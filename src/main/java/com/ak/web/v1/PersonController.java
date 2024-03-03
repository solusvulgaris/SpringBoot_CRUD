package com.ak.web.v1;

import com.ak.data.Person;
import com.ak.data.PersonDTO;
import com.ak.service.PersonService;
import com.ak.util.ResourceNoAccessException;
import com.ak.util.ResourceNotFoundException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.ak.service.PersonService.hasAccess;
import static com.ak.util.GlobalMessages.SUCCESSFULLY_CREATED;
import static com.ak.util.GlobalMessages.SUCCESSFULLY_DELETED;
import static com.ak.util.GlobalMessages.SUCCESSFULLY_UPDATED;

@RequestMapping(path = "/api/v1/persons", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PersonController {
    private final PersonService personService;

    public PersonController(@NonNull PersonService personService) {
        this.personService = personService;
    }

    /**
     * GET Persons
     * URL -> http://localhost:8082/api/v1/persons/
     *
     * @return list of all persons
     */
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Person> getPersons() {
        return new ArrayList<>(personService.getAll());
    }

    /**
     * GET Person by id
     * URL -> http://localhost:8082/api/v1/persons/{id}
     *
     * @param id - person id
     * @return requested person
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Person getPerson(@PathVariable("id") Integer id) {
        return personService.get(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * DELETE Person by id
     * URL -> http://localhost:8082/api/v1/persons/{id}
     *
     * @param id - person id
     * @return text message about successful deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable("id") Integer id) {
        boolean deleted = personService.delete(id);
        return deleted ? ResponseEntity.ok(String.format(SUCCESSFULLY_DELETED, id)) :
                new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Update Persons` name
     * PATCH = Partial update - only transmitted data
     * URL -> http://localhost:8082/api/v1/persons/{id}?name={name}
     *
     * @param id   - person id
     * @param name - person new name
     * @return text message about successful renaming
     */
    @PatchMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Person updatePerson(@PathVariable("id") Integer id, @RequestParam("name") String name) {
        return personService.update(id, name)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    /**
     * Create person or Replace if exists
     * PUT = create new or fully replace if exists
     * URL -> http://localhost:8082/api/v1/persons/{id}
     *
     * @param managerId - managers id
     * @param personDTO - person to update
     * @return text message about successful resolution
     */
    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrReplacePerson(
            @PathVariable("id") Integer managerId,
            @RequestBody PersonDTO personDTO) {
        if (hasAccess(managerId)) {
            int id = personDTO.getId();
            String name = personDTO.getName();
            return personService.update(id, name)
                    .map(p -> new ResponseEntity<>(
                            String.format(SUCCESSFULLY_UPDATED, p.getId(), p.getName()),
                            HttpStatus.OK))
                    .orElseGet(() -> {
                        personService.add(id, name);
                        return new ResponseEntity<>(
                                String.format(SUCCESSFULLY_CREATED, id, name),
                                HttpStatus.CREATED);
                    });
        } else {
            throw new ResourceNoAccessException(managerId);
        }
    }

    /**
     * Create new person
     * POST = create new even if exists ?
     * URL -> http://localhost:8082/api/v1/persons/
     *
     * @param personDTO - person to add
     * @return Person - created person
     */
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String createPerson(@RequestBody PersonDTO personDTO) {
        personService.add(personDTO.getId(), personDTO.getName());
        return String.format(SUCCESSFULLY_CREATED, personDTO.getId(), personDTO.getName());
    }
}
