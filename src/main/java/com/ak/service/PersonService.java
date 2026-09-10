package com.ak.service;

import com.ak.dto.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person create(Person person);

    void delete(long id);

    Optional<Person> get(long id);

    List<Person> getAll();

    Optional<Person> update(Person person);
}
