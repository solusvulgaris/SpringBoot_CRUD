package com.ak.service;

import com.ak.data.PersonJpaEntity;
import com.ak.dto.Person;
import com.ak.mapper.PersonJpaMapper;
import com.ak.repository.PersonJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("personServiceJpa")
@Slf4j
public class PersonServiceJpa {

    private final PersonJpaRepository personJpaRepository;
    private final PersonJpaMapper personJpaMapper;

    public PersonServiceJpa(PersonJpaRepository personJpaRepository,
                             PersonJpaMapper personJpaMapper) {
        this.personJpaRepository = personJpaRepository;
        this.personJpaMapper = personJpaMapper;
    }

    @Transactional(readOnly = true)
    public List<Person> getAllPersons() {
        log.info("Getting all persons from PostgreSQL");
        return personJpaRepository.findAll().stream()
                .map(personJpaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<Person> getPerson(long id) {
        return personJpaRepository.findById(id)
                .map(personJpaMapper::toDto);
    }

    @Transactional
    public Person savePerson(Person person) {
        PersonJpaEntity entity = personJpaMapper.toEntity(person);
        entity.setId(null);
        return personJpaMapper.toDto(personJpaRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<Person> getPersonsBySurname(String surname) {
        long start = System.nanoTime();
        List<Person> result = personJpaRepository.findBySurname(surname).stream()
                .map(personJpaMapper::toDto)
                .toList();
        log.info("Exact surname query took {} ms", elapsedMillis(start));
        return result;
    }

    @Transactional(readOnly = true)
    public List<Person> searchBySurnamePattern(String pattern) {
        long start = System.nanoTime();
        List<Person> result = personJpaRepository.findBySurnameContaining(pattern).stream()
                .map(personJpaMapper::toDto)
                .toList();
        log.info("Surname LIKE query took {} ms", elapsedMillis(start));
        return result;
    }

    @Transactional(readOnly = true)
    public List<Person> findByAgeGreaterThan(int age) {
        return personJpaRepository.findByAgeGreaterThan(age).stream()
                .map(personJpaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Person> searchBySurnameNative(String surname) {
        return personJpaRepository.searchBySurnameNative(surname).stream()
                .map(personJpaMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getSurnameStatistics() {
        return personJpaRepository.findDuplicateSurnames().stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1],
                        Long::sum
                ));
    }

    @Transactional
    public void generateTestData(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("Count must not be negative");
        }

        log.info("Generating {} test persons in PostgreSQL", count);
        List<PersonJpaEntity> persons = java.util.stream.IntStream.range(0, count)
                .mapToObj(i -> Person.builder()
                        .name("FirstName" + i)
                        .surname("LastName" + (i % 1000))
                        .age(20 + (i % 50))
                        .build())
                .map(personJpaMapper::toEntity)
                .toList();

        personJpaRepository.saveAll(persons);
    }

    private long elapsedMillis(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000;
    }
}
