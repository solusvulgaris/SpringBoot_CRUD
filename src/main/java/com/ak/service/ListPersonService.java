package com.ak.service;

import com.ak.data.Person;
import com.ak.data.PersonDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ListPersonService implements PersonService {
    private final List<Person> persons;

    @Autowired
    public ListPersonService() {
        persons = new ArrayList<>(Arrays.asList(
                new PersonDTO(1, "First"),
                new PersonDTO(2, "Second"),
                new PersonDTO(3, "Third")
        ));
    }

    public void add(int id, String name) {
        Person person = new PersonDTO(id, name);
        persons.add(person);
    }

    @Override
    public boolean delete(int id) {
        Person p = get(id)
                .orElse(new PersonDTO(id, ""));
        return persons.remove(p);
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
    public Optional<Person> update(int id, String name) {
        Optional<Person> p = get(id);
        p.ifPresent(person -> person.setName(name));
        return p;
    }
}
