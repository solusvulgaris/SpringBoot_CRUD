package com.ak.service;

import com.ak.data.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

class PersonCounterServiceImplTest {

    private static List<Person> createListWithNullElements() {
        List<Person> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        return list;
    }

    public static Stream<Arguments> sourceListIsNotNULL() {
        return Stream.of(
                Arguments.of(0, Collections.EMPTY_LIST),
                Arguments.of(3, createListWithNullElements()));
    }

    @ParameterizedTest
    @MethodSource("sourceListIsNotNULL")
    void Given_ListIsNotNULL_When_GetSize_Then_ReturnListSize(int expectedListSize, List<Person> persons) {
        Assertions.assertEquals(expectedListSize, persons.size());
    }

    @Test
    void Given_ListIsNULL_When_GetSize_Then_ThrowNullPointerException() {
        List<Person> persons = null;
        final Class<NullPointerException> expectedExceptionClass = NullPointerException.class;
        final NullPointerException actualException = Assertions.assertThrows(
                expectedExceptionClass,
                () -> persons.size()
        );

        Assertions.assertEquals(expectedExceptionClass, actualException.getClass(), "Exception class");
    }

    public static Stream<Arguments> sourceCountPersonsSurnames() {
        return Stream.of(
                Arguments.of('a', 0, null),
                Arguments.of('a', 0, Collections.EMPTY_LIST),
                Arguments.of('a', 0, createListWithNullElements()),
                Arguments.of('a', 2, new ArrayList<>(List.of(
                        Person.builder(0).build(),
                        Person.builder(0).surname("A").build(),
                        Person.builder(0).surname("a").build()
                ))),
                Arguments.of('A', 2, new ArrayList<>(List.of(
                        Person.builder(0).build(),
                        Person.builder(0).surname("A").build(),
                        Person.builder(0).surname("a").build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsSurnames")
    void Given_FirstLetter_When_CountPersonsSurnames_Then_ReturnCount(char firstLetter, int expectedCount, List<Person> persons) {
        Assertions.assertEquals(
                expectedCount,
                new PersonCounterServiceImpl().countPersonsSurnamesStartedWith(firstLetter, persons));
    }

    public static Stream<Arguments> sourceCountPersonsAverageAge() {
        return Stream.of(
                Arguments.of(0d, null),
                Arguments.of(0d, Collections.EMPTY_LIST),
                Arguments.of(0d, createListWithNullElements()),
                Arguments.of(19.0d, new ArrayList<>(List.of(
                        Person.builder(0).age(0).build(),
                        Person.builder(0).age(30).build(),
                        Person.builder(0).age(27).build()
                ))),
                Arguments.of(28.5d, new ArrayList<>(List.of(
                        Person.builder(0).build(),
                        Person.builder(0).age(30).build(),
                        Person.builder(0).age(27).build()
                ))),
                Arguments.of(28.5d, new ArrayList<>(List.of(
                        Person.builder(0).age(30).build(),
                        Person.builder(0).age(27).build()
                ))),
                Arguments.of(37.67d, new ArrayList<>(List.of(
                        Person.builder(0).age(13).build(),
                        Person.builder(0).age(50).build(),
                        Person.builder(0).age(50).build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsAverageAge")
    void Given_PersonsList_When_CountPersonsAverageAge_Then_ReturnAverageAge(double expectedAverageAge, List<Person> persons) {
        Assertions.assertEquals(
                expectedAverageAge,
                new PersonCounterServiceImpl().countPersonsAverageAge(persons));
    }

    public static Stream<Arguments> sourceGetMissingPersonsIds() {
        return Stream.of(
                Arguments.of(new ArrayList<>(), null),
                Arguments.of(new ArrayList<>(), Collections.EMPTY_LIST),
                Arguments.of(new ArrayList<>(), createListWithNullElements()),
                Arguments.of(
                        new ArrayList<>(),
                        new ArrayList<>(List.of(
                                Person.builder(0).build()
                        ))),
                Arguments.of(
                        new ArrayList<>(),
                        new ArrayList<>(List.of(
                                Person.builder(0).build(),
                                Person.builder(1).build()
                        ))),
                Arguments.of(
                        new ArrayList<>(Collections.singleton(1)),
                        new ArrayList<>(List.of(
                                Person.builder(0).build(),
                                Person.builder(2).build()
                        ))),
                Arguments.of(
                        new ArrayList<>(List.of(1, 2, 3, 4)),
                        new ArrayList<>(List.of(
                                Person.builder(0).age(30).build(),
                                Person.builder(5).age(27).build()
                        ))),
                Arguments.of(
                        new ArrayList<>(List.of(1, 2, 3, 5)),
                        new ArrayList<>(List.of(
                                Person.builder(0).age(30).build(),
                                Person.builder(4).age(27).build(),
                                Person.builder(6).age(27).build()
                        )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceGetMissingPersonsIds")
    void Given_PersonsList_When_GetMissingPersonsIds_Then_ReturnMissingIds(List<Integer> expectedMissingIds, List<Person> persons) {
        List<Integer> missingIds = new PersonCounterServiceImpl().getMissingPersonsIds(persons);
        Assertions.assertEquals(expectedMissingIds.size(), missingIds.size());

        ListIterator<Integer> iterator = missingIds.listIterator();
        while (iterator.hasNext()) {
            Assertions.assertEquals(expectedMissingIds.get(iterator.nextIndex()), iterator.next());
        }
    }

    public static Stream<Arguments> sourceCountPersonsNamesakes() {
        return Stream.of(
                Arguments.of("name", 0, null),
                Arguments.of("name", 0, Collections.EMPTY_LIST),
                Arguments.of("name", 0, createListWithNullElements()),
                Arguments.of(
                        "name", 0,
                        new ArrayList<>(List.of(
                                Person.builder(0).build()
                        ))),
                Arguments.of(
                        "name", 0,
                        new ArrayList<>(List.of(
                                Person.builder(0).name("null").build(),
                                Person.builder(1).build()
                        ))),
                Arguments.of(
                        "name", 1,
                        new ArrayList<>(List.of(
                                Person.builder(0).name("null").build(),
                                Person.builder(0).name("name").build(),
                                Person.builder(2).build()
                        ))),
                Arguments.of(
                        "name", 2,
                        new ArrayList<>(List.of(
                                Person.builder(0).name("null").build(),
                                Person.builder(5).name("name").build(),
                                Person.builder(0).name("name").build(),
                                Person.builder(2).build()
                        )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsNamesakes")
    void Given_NameAndPersonsList_When_CountPersonsNamesakes_Then_ReturnCount(String name, int expectedCount, List<Person> persons) {
        Assertions.assertEquals(expectedCount, new PersonCounterServiceImpl().countPersonsNamesakes(name, persons));
    }
}