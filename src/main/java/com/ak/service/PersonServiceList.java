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
                new Person(1, "First", "Surname-1", 30),
                new Person(2, "Second", "Surname-2", 28),
                new Person(3, "Third", "Surname-3", 15)
        ));
    }

    @Override
    public Person create(int id, String name, String surname, Integer age) {
        if(persons.stream().map(Person::getId).anyMatch(pId -> pId == id)) {
            throw new ResourceAlreadyExistException(id);
        }
        Person person = new Person(id, name, surname, age);
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
