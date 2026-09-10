package com.ak.service;

import com.ak.data.PersonListEntity;
import com.ak.dto.Person;
import com.ak.mapper.PersonListMapper;
import com.ak.util.ResourceAlreadyExistException;
import com.ak.util.ResourceNotDeletedException;
import com.ak.util.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceList implements PersonService {
    private final List<PersonListEntity> persons;
    private final PersonListMapper personListMapper;

    public PersonServiceList(PersonListMapper personListMapper) {
        this.personListMapper = personListMapper;
        persons = new ArrayList<>(Arrays.asList(
                PersonListEntity.builder(1).name("First").surname("Surname-1").age(30).build(),
                PersonListEntity.builder(2).name("Second").surname("Surname-2").age(28).build(),
                PersonListEntity.builder(3).name("Third").surname("Surname-3").age(15).build()
        ));
    }

    @Override
    public Person create(Person person) {
        long id = person.getId();
        if (persons.stream().map(PersonListEntity::getId).anyMatch(personId -> personId == id)) {
            throw new ResourceAlreadyExistException(id);
        }

        PersonListEntity entity = personListMapper.toEntity(person);
        persons.add(entity);
        return personListMapper.toDto(entity);
    }

    @Override
    public void delete(long id) {
        PersonListEntity entity = find(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        if (!persons.remove(entity)) {
            throw new ResourceNotDeletedException(id);
        }
    }

    @Override
    public List<Person> getAll() {
        return persons.stream().map(personListMapper::toDto).toList();
    }

    @Override
    public Optional<Person> get(long id) {
        return find(id).map(personListMapper::toDto);
    }

    private Optional<PersonListEntity> find(long id) {
        return persons.stream()
                .filter(person -> person.getId() == id)
                .findFirst();
    }

    @Override
    public Optional<Person> update(Person person) {
        Optional<PersonListEntity> entity = find(person.getId());
        entity.ifPresent(value -> {
            value.setName(person.getName());
            value.setSurname(person.getSurname());
            value.setAge(person.getAge());
        });
        return entity.map(personListMapper::toDto);
    }
}
