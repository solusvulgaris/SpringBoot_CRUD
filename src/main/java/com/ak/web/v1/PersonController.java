package com.ak.web.v1;

import com.ak.data.Person;
import com.ak.service.PersonService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RequestMapping(path = "/personapp/v1/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(@NonNull PersonService personService) {
        this.personService = personService;
    }

    /**
     * GET Persons
     * URL -> http://localhost:8082/personapp/v1/persons/
     *
     * @return list of all persons
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Person> getAll() {
        return new ArrayList<>(personService.getAll());
    }

    /**
     * GET Person by id
     * URL -> http://localhost:8082/personapp/v1/persons/{id}
     *
     * @param id - person id
     * @return requested person
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> get(@PathVariable("id") int id) {
        return ResponseEntity.of(personService.get(id));
    }

    /**
     * DELETE Person by id
     * URL -> http://localhost:8082/personapp/v1/persons/{id}
     *
     * @param id - person id
     * @return text message about successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") int id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Create Person or Replace if exists
     * PUT = create new or fully replace if exists
     * URL -> http://localhost:8082/personapp/v1/persons/{id}
     *
     * @param id     - persons` id to get object (if too many fields)
     * @param person - person with data to update
     * @return text message about successful resolution
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createOrReplace(
            @PathVariable("id") int id,
            @RequestBody Person person) {
        if (id == person.getId()) {

            return personService.update(person.getId(), person.getName(), person.getSurname(), person.getAge())
                    .map(p -> ResponseEntity.accepted().build())
                    .orElseGet(() -> {
                        personService.create(person.getId(), person.getName(), person.getSurname(), person.getAge());
                        URI personURI = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(person.getId())
                                .toUri();
                        return ResponseEntity.created(personURI).build();
                    });
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Create new Person
     * POST = can create new even if exists
     * URL -> http://localhost:8082/personapp/v1/persons/
     *
     * @param id   - new person id
     * @param name - new person name
     * @param surname - new person sername
     * @param age - new person age
     * @return Person - created person
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Person create(
            @PathVariable("id") int id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("age") Integer age) {
        return personService.create(id, name, surname, age);
    }
}
