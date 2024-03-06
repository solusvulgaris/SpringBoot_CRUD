package com.ak.service;

import com.ak.data.Person;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

@Service
public class PersonCounterServiceImpl implements PersonCounterService {

    @Override
    public int countPersonsSurnamesStartedWith(char firstLetter, List<Person> persons) {
        if (persons != null) {
            long count = persons.stream()
                    .filter(Objects::nonNull)
                    .map(Person::getSurname)
                    .filter(s -> !StringUtils.isBlank(s))
                    .map(String::toLowerCase)
                    .filter(s -> s.startsWith(String.valueOf(firstLetter).toLowerCase()))
                    .count();
            return (int) count;
        }
        return 0;
    }

    @Override
    public double countPersonsAverageAge(List<Person> persons) {
        if (persons == null) {//list can be null
            return 0;
        }

        double totalPersonsAge = persons.stream()
                .filter(Objects::nonNull)//person can be null
                .map(Person::getAge)//age can be null
                .filter(age -> !Objects.isNull(age))
                .reduce(0, Integer::sum).doubleValue();

        double numberOfNotNullPersons = persons.stream()
                .filter(Objects::nonNull)//not include null person
                .map(Person::getAge)
                .filter(age -> !Objects.isNull(age))//not include persons with null age -> age not specified
                .count();

        return numberOfNotNullPersons > 0 ? totalPersonsAge / numberOfNotNullPersons : 0;
    }

    @Override
    public List<Integer> getMissingPersonsIds(List<Person> persons) {
        if (persons == null) {
            return new ArrayList<>();
        }

        List<Integer> existingIds = persons.stream()
                .filter(Objects::nonNull)
                .map(Person::getId)
                .sorted().toList();

        int firstId = existingIds.stream().min(Integer::compareTo).orElse(0);
        int lastId = existingIds.stream().max(Integer::compareTo).orElse(0);

        List<Integer> continuousIds = new ArrayList<>(IntStream.iterate(firstId, i -> i + 1)
                .limit(lastId)
                .boxed().toList());

        continuousIds.removeAll(existingIds);
        return continuousIds;
    }
}
