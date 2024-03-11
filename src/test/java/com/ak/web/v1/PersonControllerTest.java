package com.ak.web.v1;

import com.ak.data.Person;
import com.ak.service.PersonService;
import com.ak.util.ResourceAlreadyExistException;
import com.ak.util.ResourceNotDeletedException;
import com.ak.util.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    public static final String URL_TEMPLATE = "/personapp/v1/persons/{id}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    private static final Person testPerson = Person.builder(10).name("Name").surname("Surname").age(33).build();
    private static String personAsJsonString;

    private static String getObjectAsJsonString(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private static String getJsonRequestBody(Person person) {
        String json = "{\"id\":" + person.getId();
        json += Optional.ofNullable(person.getName()).map(p -> ",\"name\":\"" + p + "\"").orElse("");
        json += Optional.ofNullable(person.getSurname()).map(p -> ",\"surname\":\"" + p + "\"").orElse("");
        json += Optional.ofNullable(person.getAge()).map(p -> ",\"age\":" + p).orElse("");
        return json + "}";
    }

    @BeforeAll
    static void beforeAll() throws JsonProcessingException {
        personAsJsonString = getObjectAsJsonString(testPerson);
    }

    @Test
    void Given_AnyConditions_When_GetAll_Then_IsOk() throws Exception {
        List<Person> testPersons = new ArrayList<>(Arrays.asList(
                testPerson,
                testPerson));
        when(personService.getAll())
                .thenReturn(testPersons);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/personapp/v1/persons/")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(getObjectAsJsonString(testPersons)));
    }

    @Test
    void Given_Exists_When_Get_Then_IsOk() throws Exception {
        when(personService.get(testPerson.getId()))
                .thenReturn(Optional.of(testPerson));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL_TEMPLATE, testPerson.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(personAsJsonString));
    }

    @Test
    void Given_NotExists_When_Get_Then_IsNotFound() throws Exception {
        when(personService.get(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL_TEMPLATE, 1)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void Given_Exists_When_Delete_Then_IsNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL_TEMPLATE, 1))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void Given_NotExists_When_Delete_Then_IsNotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException(1))
                .when(personService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL_TEMPLATE, 1))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void Given_NotDeleted_When_Delete_Then_IsBadRequest() throws Exception {
        Mockito.doThrow(new ResourceNotDeletedException(1))
                .when(personService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL_TEMPLATE, 1))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }


    @Test
    void Given_IdsDoNotMatch_When_CreateOrReplace_Then_IsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE, testPerson.getId() + 1)
                        .content(personAsJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void Given_DoesNotExist_When_CreateOrReplace_Then_IsCreated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE, testPerson.getId())
                        .content(personAsJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl("http://localhost/personapp/v1/persons/10/10"));
    }

    @Test
    void Given_ExistsReceivesFullData_When_CreateOrReplace_Then_IsAccepted() throws Exception {
        when(personService.update(testPerson.getId(), testPerson.getName(), testPerson.getSurname(), testPerson.getAge()))
                .thenReturn(Optional.of(testPerson));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE, testPerson.getId())
                        .content(personAsJsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    public static Stream<Arguments> sourceCreateOrReplacePartialIsAccepted() {
        return Stream.of(
                Arguments.of(Person.builder(0).build()),
                Arguments.of(Person.builder(0).name("name").build()),
                Arguments.of(Person.builder(0).name("name").surname("surname").build()),
                Arguments.of(testPerson)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCreateOrReplacePartialIsAccepted")
    void Given_ExistsReceivesPartialData_When_CreateOrReplace_Then_IsAccepted(Person person) throws Exception {
        when(personService.update(person.getId(), person.getName(), person.getSurname(), person.getAge()))
                .thenReturn(Optional.of(person));

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL_TEMPLATE, person.getId())
                        .content(getJsonRequestBody(person))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    public static Stream<Arguments> sourceCreateIsCreated() {
        return Stream.of(
                Arguments.of(Person.builder(0).build()),
                Arguments.of(Person.builder(0).name("name").build()),
                Arguments.of(Person.builder(0).name("name").surname("surname").build()),
                Arguments.of(testPerson)
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCreateIsCreated")
    void Given_NotExistsReceivesVariousData_When_Create_Then_IsCreated(Person person) throws Exception {
        when(personService.create(person.getId(), person.getName(), person.getSurname(), person.getAge()))
                .thenReturn(person);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Optional.ofNullable(person.getName()).ifPresent(p -> params.add("name", p));
        Optional.ofNullable(person.getSurname()).ifPresent(p -> params.add("surname", p));
        Optional.ofNullable(person.getAge()).ifPresent(p -> params.add("age", String.valueOf(p)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL_TEMPLATE, person.getId())
                        .params(params))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(getObjectAsJsonString(person)));
    }

    @Test
    void Given_Exists_When_Create_Then_IsConflict() throws Exception {
        Mockito.doThrow(new ResourceAlreadyExistException(1))
                .when(personService).create(0, null, null, null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL_TEMPLATE, 0)
                        .params(new LinkedMultiValueMap<>()))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}