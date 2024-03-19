package com.ak.service;

import com.ak.data.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    Person create(int id, String name, String surname, Integer age);

    void delete(int id);

    Optional<Person> get(int id);

    List<Person> getAll();

    Optional<Person> update(int id, String name, String surname, Integer age);
}
