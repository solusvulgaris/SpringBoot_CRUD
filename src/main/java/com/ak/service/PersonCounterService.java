package com.ak.service;

import com.ak.data.Person;

import java.util.List;

/**
 * Service provide operations on a list of persons
 */
public interface PersonCounterService {

    /**
     * Count persons surnames begins with provided letter
     * This method is case-insensitive
     *
     * @param firstLetter - provided letter
     * @param persons     - persons list
     * @return number of persons whose surname begins with a provided letter
     */
    int countPersonsSurnamesStartedWith(char firstLetter, List<Person> persons);

    /**
     * Count average persons age;
     *
     * @param persons       - persons list
     * @return average age of all persons in provided list (null objects is not considered)
     */
    double countPersonsAverageAge(List<Person> persons);

    /**
     * Get missing persons ids,
     * example:{1,3,5} -> missing 2 & 4
     *
     * @param persons       - persons list
     * @return return missing ids
     */
    List<Integer> getMissingPersonsIds(List<Person> persons);
}
