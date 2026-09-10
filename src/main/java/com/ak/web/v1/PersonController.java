package com.ak.web.v1;

import com.ak.dto.Person;
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
        // return new ArrayList<>(personService.getAll()); - TODO: почему была обертка?
        return personService.getAll();
    }

    /**
     * GET Person by id
     * URL -> http://localhost:8082/personapp/v1/persons/{id}
     *
     * @param id - person id
     * @return requested person
     */
    @GetMapping("/{id}")
    public ResponseEntity<Person> get(@PathVariable("id") long id) {
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
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
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
            @PathVariable("id") long id,
            @RequestBody Person person) {
        if (person.getId() != null && id == person.getId()) {
            return personService.update(person)
                    .map(p -> ResponseEntity.accepted().build())
                    .orElseGet(() -> {
                        personService.create(person);
                        URI personUri = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(person.getId())
                                .toUri();
                        return ResponseEntity.created(personUri).build();
                    });
        }
        return ResponseEntity.badRequest().build();
    }

    /**
     * Create new Person
     * POST = can create new even if exists
     * URL -> http://localhost:8082/personapp/v1/persons/
     *
     * @param id      - new person id
     * @param name    - new person name
     * @param surname - new person surname
     * @param age     - new person age
     * @return Person - created person
     */
    @PostMapping("/{id}")//TODO: delete id -> generate by service!
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Person create(
            @PathVariable("id") long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "surname", required = false) String surname,
            @RequestParam(value = "age", required = false) Integer age) {
        return personService.create(Person.builder()
                .id(id)
                .name(name)
                .surname(surname)
                .age(age)
                .build());
    }
}
