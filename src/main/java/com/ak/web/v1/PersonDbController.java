package com.ak.web.v1;

import com.ak.dto.Person;
import com.ak.service.PersonServiceJpa;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/personapp/v1/db/persons")
public class PersonDbController {

    private final PersonServiceJpa personServiceJpa;

    public PersonDbController(@NonNull PersonServiceJpa personServiceJpa) {
        this.personServiceJpa = personServiceJpa;
    }

    @GetMapping
    public List<Person> getAllPersons() {
        return personServiceJpa.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable long id) {
        Optional<Person> person = personServiceJpa.getPerson(id);
        return ResponseEntity.of(person);
    }

    @GetMapping("/by-surname")
    public List<Person> getBySurname(@RequestParam String surname) {
        return personServiceJpa.getPersonsBySurname(surname);
    }

    @GetMapping("/search")
    public List<Person> searchBySurnamePattern(@RequestParam String pattern) {
        return personServiceJpa.searchBySurnamePattern(pattern);
    }

    @GetMapping("/by-age")
    public List<Person> findByAgeGreaterThan(@RequestParam int age) {
        return personServiceJpa.findByAgeGreaterThan(age);
    }

    @GetMapping("/native-search")
    public List<Person> searchBySurnameNative(@RequestParam String surname) {
        return personServiceJpa.searchBySurnameNative(surname);
    }

    @GetMapping("/stats/surnames")
    public Map<String, Long> getSurnameStatistics() {
        return personServiceJpa.getSurnameStatistics();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateTestData(@RequestParam int count) {
        personServiceJpa.generateTestData(count);
        return ResponseEntity.ok("Generated " + count + " persons");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person createPerson(@RequestBody Person person) {
        return personServiceJpa.savePerson(person);
    }
}
