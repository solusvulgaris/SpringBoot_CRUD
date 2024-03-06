package com.ak.data;

import lombok.Data;

@Data
public class Person {
    private int id;
    private String name;
    private String surname;
    private Integer age;

    public Person(int id, String name, String surname, Integer age) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    public Person() {

    }
}
