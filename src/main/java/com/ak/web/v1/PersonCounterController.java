package com.ak.web.v1;

import com.ak.data.Person;
import com.ak.service.PersonCounterService;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping(path = "/personapp/v1/personscounter", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class PersonCounterController {
    private final PersonCounterService personCounterService;

    public PersonCounterController(@NonNull PersonCounterService personCounterService) {
        this.personCounterService = personCounterService;
    }

    @GetMapping(path = "/missing-ids", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Integer> getMissingIds(
            @RequestBody List<Person> persons) {
        return personCounterService.getMissingPersonsIds(persons);
    }

    @GetMapping(path = "/averageage", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody double getAverageAge(
            @RequestBody List<Person> persons) {
        return personCounterService.countPersonsAverageAge(persons);
    }

    @GetMapping(path = "/namesakes", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody int getPersonsNamesakesCount(
            @RequestParam(value = "name") String name,
            @RequestBody List<Person> persons) {
        return personCounterService.countPersonsNamesakes(name, persons);
    }

    @GetMapping(path = "/surnames", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody int getPersonsSurnamesCount(
            @RequestParam(value = "letter") char letter,
            @RequestBody List<Person> persons) {
        return personCounterService.countPersonsSurnamesStartedWith(letter, persons);
    }
}
