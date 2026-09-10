package com.ak.mapper;

import com.ak.data.PersonListEntity;
import com.ak.dto.Person;
import org.springframework.stereotype.Component;

@Component
public class PersonListMapper {

    public PersonListEntity toEntity(Person person) {
        if (person == null) {
            return null;
        }

        return PersonListEntity.builder(person.getId())
                .name(person.getName())
                .surname(person.getSurname())
                .age(person.getAge())
                .build();
    }

    public Person toDto(PersonListEntity entity) {
        if (entity == null) {
            return null;
        }

        return Person.builder()
                .id(entity.getId())
                .name(entity.getName())
                .surname(entity.getSurname())
                .age(entity.getAge())
                .build();
    }
}
