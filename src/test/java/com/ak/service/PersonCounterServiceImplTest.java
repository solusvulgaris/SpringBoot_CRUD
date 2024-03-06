package com.ak.service;

import com.ak.data.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

class PersonCounterServiceImplTest {

    private static List<Person> createListWithNullElements() {
        List<Person> list = new ArrayList<>();
        list.add(null);
        list.add(null);
        list.add(null);
        return list;
    }

    public static Stream<Arguments> sourceCheckListSize() {
        return Stream.of(
                Arguments.of(0, null),
                Arguments.of(0, Collections.EMPTY_LIST),
                Arguments.of(3, createListWithNullElements()));
    }

    @ParameterizedTest
    @MethodSource("sourceCheckListSize")
    @DisplayName("checkListSize test cases")
    void checkListSizeTest(int expectedListSize, List<Person> persons) {
        if (persons == null) {
            final Class<NullPointerException> expectedExceptionClass = NullPointerException.class;
            final NullPointerException actualException = Assertions.assertThrows(
                    expectedExceptionClass,
                    () -> persons.size()
            );

            Assertions.assertEquals(expectedExceptionClass, actualException.getClass(), "Exception class");
        } else {
            Assertions.assertEquals(expectedListSize, persons.size());
        }
    }

    public static Stream<Arguments> sourceCountPersonsSurnamesStartsWith() {
        return Stream.of(
                Arguments.of('a', 0, null),
                Arguments.of('a', 0, Collections.EMPTY_LIST),
                Arguments.of('a', 0, createListWithNullElements()),
                Arguments.of('a', 2, new ArrayList<>(List.of(
                        new Person(0, null, null, null),
                        new Person(0, null, "A", null),
                        new Person(0, null, "a", null)
                ))),
                Arguments.of('A', 2, new ArrayList<>(List.of(
                        new Person(0, null, null, null),
                        new Person(0, null, "A", null),
                        new Person(0, null, "a", null)
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsSurnamesStartsWith")
    @DisplayName("countPersonsSurnamesStartsWith() test cases")
    void countPersonsSurnamesStartsWithTest(char firstLetter, int expectedCount, List<Person> persons) {
        Assertions.assertEquals(
                expectedCount,
                new PersonCounterServiceImpl().countPersonsSurnamesStartedWith(firstLetter, persons));
    }

    public static Stream<Arguments> sourceCountPersonsAverageAge() {
        return Stream.of(
                Arguments.of(0d, null),
                Arguments.of(0d, Collections.EMPTY_LIST),
                Arguments.of(0d, createListWithNullElements()),
                Arguments.of(19.0d, new ArrayList<>(List.of(
                        new Person(0, null, null, 0),
                        new Person(0, null, null, 30),
                        new Person(0, null, null, 27)
                ))),
                Arguments.of(28.5d, new ArrayList<>(List.of(
                        new Person(0, null, null, null),
                        new Person(0, null, null, 30),
                        new Person(0, null, null, 27)
                ))),
                Arguments.of(28.5d, new ArrayList<>(List.of(
                        new Person(0, null, null, 30),
                        new Person(0, null, null, 27)
                )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCountPersonsAverageAge")
    @DisplayName("countPersonsAverageAge() test cases")
    void countPersonsAverageAge(double expectedAverageAge, List<Person> persons) {
        Assertions.assertEquals(
                expectedAverageAge,
                new PersonCounterServiceImpl().countPersonsAverageAge(persons));
    }

    public static Stream<Arguments> sourceMissingPersonsIds() {
        return Stream.of(
                Arguments.of(new ArrayList<>(), null),
                Arguments.of(new ArrayList<>(), Collections.EMPTY_LIST),
                Arguments.of(new ArrayList<>(), createListWithNullElements()),
                Arguments.of(
                        new ArrayList<>(),
                        new ArrayList<>(List.of(
                                new Person(0, null, null, null)
                        ))),
                Arguments.of(
                        new ArrayList<>(),
                        new ArrayList<>(List.of(
                                new Person(0, null, null, null),
                                new Person(1, null, null, null)
                        ))),
                Arguments.of(
                        new ArrayList<>(Collections.singleton(1)),
                        new ArrayList<>(List.of(
                                new Person(0, null, null, null),
                                new Person(2, null, null, null)
                        ))),
                Arguments.of(
                        new ArrayList<>(List.of(1, 2, 3, 4)),
                        new ArrayList<>(List.of(
                                new Person(0, null, null, 30),
                                new Person(5, null, null, 27)
                        ))),
                Arguments.of(
                        new ArrayList<>(List.of(1, 2, 3, 5)),
                        new ArrayList<>(List.of(
                                new Person(0, null, null, 30),
                                new Person(4, null, null, 27),
                                new Person(6, null, null, 27)
                        )))
        );
    }

    @ParameterizedTest
    @MethodSource("sourceMissingPersonsIds")
    @DisplayName("getMissingPersonsIds() test cases")
    void getMissingPersonsIds(List<Integer> expectedMissingIds, List<Person> persons) {
        List<Integer> missingIds = new PersonCounterServiceImpl().getMissingPersonsIds(persons);
        Assertions.assertEquals(expectedMissingIds.size(), missingIds.size());
        for (int i = 0; i < expectedMissingIds.size(); i++) {
            Assertions.assertEquals(expectedMissingIds.get(i), missingIds.get(i));
        }
    }
}