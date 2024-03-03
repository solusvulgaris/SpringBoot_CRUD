package com.ak.service;

import com.ak.data.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    void add(int id, String name);

    boolean delete(int id);

    Optional<Person> get(int id);

    List<Person> getAll();

    Optional<Person> update(int id, String name);

    static boolean hasAccess(int id) {
        return id == 1;
    }
}
