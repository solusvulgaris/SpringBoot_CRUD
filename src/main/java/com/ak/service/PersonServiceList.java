package com.ak.service;

import com.ak.data.Person;
import com.ak.util.ResourceAlreadyExistException;
import com.ak.util.ResourceNotDeletedException;
import com.ak.util.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceList implements PersonService {
    private final List<Person> persons;

    @Autowired
    public PersonServiceList() {
        persons = new ArrayList<>(Arrays.asList(
                Person.builder(1).name("First").surname("Surname-1").age(30).build(),
                Person.builder(2).name("Second").surname("Surname-2").age(28).build(),
                Person.builder(3).name("Third").surname("Surname-3").age(15).build()
        ));
    }

    @Override
    public Person create(int id, String name, String surname, Integer age) {
        if(persons.stream().map(Person::getId).anyMatch(pId -> pId == id)) {
            throw new ResourceAlreadyExistException(id);
        }
        Person person = Person.builder(id).name(name).surname(surname).age(age).build();
        persons.add(person);
        return person;
    }

    @Override
    public void delete(int id) {
        Person p = get(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if(!persons.remove(p)) {
            throw new ResourceNotDeletedException(id);
        }
    }

    @Override
    public List<Person> getAll() {
        return persons;
    }

    @Override
    public Optional<Person> get(int id) {
        return persons.stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Person> update(int id, String name, String surname, Integer age) {
        Optional<Person> p = get(id);
        p.ifPresent(person -> person.setName(name));
        return p;
    }
}
