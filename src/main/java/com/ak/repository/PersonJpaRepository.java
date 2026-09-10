package com.ak.repository;

import com.ak.data.PersonJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonJpaRepository extends JpaRepository<PersonJpaEntity, Long> {

    /**
     * Derived query with an exact surname match.
     */
    // 1. Метод с использованием JPA (автоматический запрос)
    List<PersonJpaEntity> findBySurname(String surname);

    /**
     * Derived query with a contains condition.
     * A leading wildcard can prevent PostgreSQL from using a regular B-tree index.
     */
    // 2. Запрос с LIKE (потенциально медленный)
    List<PersonJpaEntity> findBySurnameContaining(String surnamePart);

    /**
     * JPQL query for comparing ORM-generated SQL with native SQL.
     */
    // 3. Запрос с JPQL (для демонстрации N+1)
    @Query("select p from PersonJpaEntity p where p.age > :age")
    List<PersonJpaEntity> findByAgeGreaterThan(@Param("age") Integer age);

    /**
     * Native PostgreSQL query for case-insensitive surname search.
     */
    // 4. Native query (для сравнения)
    @Query(value = "select * from persons "
            + "where surname ilike concat('%', :surname, '%')",
            nativeQuery = true)
    List<PersonJpaEntity> searchBySurnameNative(@Param("surname") String surname);

    /**
     * Groups surnames that occur more than once.
     * Each result contains surname and the corresponding count.
     */
    // 5. Группировка для демонстрации сложных запросов
    @Query("select p.surname, count(p) "
            + "from PersonJpaEntity p "
            + "group by p.surname "
            + "having count(p) > 1")
    List<Object[]> findDuplicateSurnames();
}
