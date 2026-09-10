package com.ak.mapper;

import com.ak.data.PersonJpaEntity;
import com.ak.dto.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonJpaMapper {

    public PersonJpaEntity toEntity(Person person) {
        if (person == null) {
            return null;
        }

        PersonJpaEntity entity = new PersonJpaEntity();
        entity.setId(person.getId());
        entity.setName(person.getName());
        entity.setSurname(person.getSurname());
        entity.setAge(person.getAge());
        entity.setCreatedAt(person.getCreatedAt());
        return entity;
    }

    public Person toDto(PersonJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Person.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .age(entity.getAge())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
