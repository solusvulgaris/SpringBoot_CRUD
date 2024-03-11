package com.ak.data;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder(builderMethodName = "personBuilder")
public class Person {
    @NonNull
    private final int id;
    private String name;
    private String surname;
    private Integer age;

    public static PersonBuilder builder(int id) {
        return personBuilder().id(id);
    }
}
