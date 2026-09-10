package com.ak.service;

import com.ak.dto.Person;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PersonCounterServiceImplTest {

    private static List<Person> createListWithNullElements() {
        List<Person> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        return list;
    }

    public static Stream<Arguments> Given_ListIsNotNULL_When_GetSize_Then_ReturnListSize() {
        return Stream.of(
            Arguments.of(0, Collections.EMPTY_LIST),
            Arguments.of(3, createListWithNullElements()));
    }

    @ParameterizedTest
    @MethodSource
    void Given_ListIsNotNULL_When_GetSize_Then_ReturnListSize(int expectedListSize, List<Person> persons) {
        Assertions.assertEquals(expectedListSize, persons.size());
    }

    @Test
    void Given_ListIsNULL_When_GetSize_Then_ThrowNullPointerException() {
        List<Person> persons = null;
        final Class<NullPointerException> expectedExceptionClass = NullPointerException.class;
        final NullPointerException actualException = Assertions.assertThrows(
            expectedExceptionClass,
            () -> persons.size()
        );

        Assertions.assertEquals(expectedExceptionClass, actualException.getClass(), "Exception class");
    }

    public static Stream<Arguments> Given_FirstLetter_When_CountPersonsSurnames_Then_ReturnCount() {
        return Stream.of(
            Arguments.of('a', 0, null),
            Arguments.of('a', 0, Collections.EMPTY_LIST),
            Arguments.of('a', 0, createListWithNullElements()),
            Arguments.of('a', 2, new ArrayList<>(List.of(
                Person.builder().id(0L).build(),
                Person.builder().id(0L).surname("A").build(),
                Person.builder().id(0L).surname("a").build()
            ))),
            Arguments.of('A', 2, new ArrayList<>(List.of(
                Person.builder().id(0L).build(),
                Person.builder().id(0L).surname("A").build(),
                Person.builder().id(0L).surname("a").build()
            )))
        );
    }

    @ParameterizedTest
    @MethodSource
    void Given_FirstLetter_When_CountPersonsSurnames_Then_ReturnCount(char firstLetter, int expectedCount, List<Person> persons) {
        Assertions.assertEquals(
            expectedCount,
            new PersonCounterServiceImpl().countPersonsSurnamesStartedWith(firstLetter, persons));
    }

    public static Stream<Arguments> Given_PersonsList_When_CountPersonsAverageAge_Then_ReturnAverageAge() {
        return Stream.of(
            Arguments.of(0d, null),
            Arguments.of(0d, Collections.EMPTY_LIST),
            Arguments.of(0d, createListWithNullElements()),
            Arguments.of(19.0d, new ArrayList<>(List.of(
                Person.builder().id(0L).age(0).build(),
                Person.builder().id(0L).age(30).build(),
                Person.builder().id(0L).age(27).build()
            ))),
            Arguments.of(28.5d, new ArrayList<>(List.of(
                Person.builder().id(0L).build(),
                Person.builder().id(0L).age(30).build(),
                Person.builder().id(0L).age(27).build()
            ))),
            Arguments.of(28.5d, new ArrayList<>(List.of(
                Person.builder().id(0L).age(30).build(),
                Person.builder().id(0L).age(27).build()
            ))),
            Arguments.of(37.67d, new ArrayList<>(List.of(
                Person.builder().id(0L).age(13).build(),
                Person.builder().id(0L).age(50).build(),
                Person.builder().id(0L).age(50).build()
            )))
        );
    }

    @ParameterizedTest
    @MethodSource
    void Given_PersonsList_When_CountPersonsAverageAge_Then_ReturnAverageAge(double expectedAverageAge, List<Person> persons) {
        Assertions.assertEquals(
            expectedAverageAge,
            new PersonCounterServiceImpl().countPersonsAverageAge(persons));
    }

    public static Stream<Arguments> sourceGetMissingPersonsIds() {
        return Stream.of(
            Arguments.of(new ArrayList<>(), null),
            Arguments.of(new ArrayList<>(), Collections.EMPTY_LIST),
            Arguments.of(new ArrayList<>(), createListWithNullElements()),
            Arguments.of(
                new ArrayList<>(),
                new ArrayList<>(List.of(
                    Person.builder().id(0L).build()
                ))),
            Arguments.of(
                new ArrayList<>(),
                new ArrayList<>(List.of(
                    Person.builder().id(0L).build(),
                    Person.builder().id(1L).build()
                ))),
            Arguments.of(
                new ArrayList<>(Collections.singleton(1)),
                new ArrayList<>(List.of(
                    Person.builder().id(0L).build(),
                    Person.builder().id(2L).build()
                ))),
            Arguments.of(
                new ArrayList<>(List.of(1, 2, 3, 4)),
                new ArrayList<>(List.of(
                    Person.builder().id(0L).age(30).build(),
                    Person.builder().id(5L).age(27).build()
                ))),
            Arguments.of(
                new ArrayList<>(List.of(1, 2, 3, 5)),
                new ArrayList<>(List.of(
                    Person.builder().id(0L).age(30).build(),
                    Person.builder().id(4L).age(27).build(),
                    Person.builder().id(6L).age(27).build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceGetMissingPersonsIds")
    void Given_PersonsList_When_GetMissingPersonsIds_Then_ReturnMissingIds(List<Integer> expectedMissingIds, List<Person> persons) {
        List<Integer> missingIds = new PersonCounterServiceImpl().getMissingPersonsIds(persons);
        Assertions.assertEquals(expectedMissingIds.size(), missingIds.size());

        ListIterator<Integer> iterator = missingIds.listIterator();
        while (iterator.hasNext()) {
            Assertions.assertEquals(expectedMissingIds.get(iterator.nextIndex()), iterator.next());
        }
    }

    public static Stream<Arguments> sourceCountPersonsNamesakes() {
        return Stream.of(
            Arguments.of("name", 0, null),
            Arguments.of("name", 0, Collections.EMPTY_LIST),
            Arguments.of("name", 0, createListWithNullElements()),
            Arguments.of(
                "name", 0,
                new ArrayList<>(List.of(
                    Person.builder().id(0L).build()
                ))),
            Arguments.of(
                "name", 0,
                new ArrayList<>(List.of(
                    Person.builder().id(0L).name("null").build(),
                    Person.builder().id(1L).build()
                ))),
            Arguments.of(
                "name", 1,
                new ArrayList<>(List.of(
                    Person.builder().id(0L).name("null").build(),
                    Person.builder().id(0L).name("name").build(),
                    Person.builder().id(2L).build()
                ))),
            Arguments.of(
                "name", 2,
                new ArrayList<>(List.of(
                    Person.builder().id(0L).name("null").build(),
                    Person.builder().id(5L).name("name").build(),
                    Person.builder().id(0L).name("name").build(),
                    Person.builder().id(2L).build()
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsNamesakes")
    void Given_NameAndPersonsList_When_CountPersonsNamesakes_Then_ReturnCount(String name, int expectedCount, List<Person> persons) {
        Assertions.assertEquals(expectedCount, new PersonCounterServiceImpl().countPersonsNamesakes(name, persons));
    }
}
