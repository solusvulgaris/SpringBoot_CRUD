package com.ak.service;

import com.ak.data.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class PersonCounterServiceImpl implements PersonCounterService {

    @Override
    public int countPersonsSurnamesStartedWith(char firstLetter, List<Person> persons) {
        return Optional.ofNullable(persons).map(p -> p.stream()
                        .filter(Objects::nonNull)
                        .map(Person::getSurname)
                        .filter(StringUtils::isNotBlank)
                        .map(String::toLowerCase)
                        .filter(s -> s.startsWith(String.valueOf(firstLetter).toLowerCase()))
                        .count()
                ).orElse(0L)
                .intValue();
    }

    @Override
    public double countPersonsAverageAge(List<Person> persons) {
        return Optional.ofNullable(persons)
                .map(pL -> BigDecimal.valueOf(
                                        pL.stream()
                                                .filter(Objects::nonNull)//not include null person
                                                .filter(p -> Objects.nonNull(p.getAge()))//not include persons with null age -> age not specified
                                                .mapToInt(Person::getAge)
                                                .average()
                                                .orElse(0d)
                                )
                                .setScale(2, RoundingMode.HALF_UP)
                                .doubleValue()
                ).orElse(0d);
    }

    @Override
    public List<Integer> getMissingPersonsIds(List<Person> persons) {
        List<Integer> existingIds = Optional.ofNullable(persons)
                .map(p -> p.stream()
                        .filter(Objects::nonNull)
                        .map(Person::getId)
                        .sorted().toList())
                .orElse(new ArrayList<>());

        int firstId = existingIds.stream().min(Integer::compareTo).orElse(0);
        int lastId = existingIds.stream().max(Integer::compareTo).orElse(0);

        List<Integer> continuousIds = new ArrayList<>(IntStream.iterate(firstId, i -> i + 1)
                .limit(lastId)
                .boxed().toList());

        continuousIds.removeAll(existingIds);
        return continuousIds;
    }

    @Override
    public int countPersonsNamesakes(final String name, List<Person> persons) {
        return Optional.ofNullable(persons)
                .map(p -> p.stream()
                        .filter(Objects::nonNull)
                        .map(Person::getName)
                        .filter(StringUtils::isNotBlank)
                        .filter(n -> n.equals(name))
                        .count()
                ).orElse(0L)
                .intValue();
    }
}
