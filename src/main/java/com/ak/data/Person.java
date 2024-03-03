package com.ak.data;

import lombok.Getter;
import lombok.Setter;

public abstract class Person {
    @Getter
    private final int id;
    @Getter
    @Setter
    private String name;

    protected Person(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
