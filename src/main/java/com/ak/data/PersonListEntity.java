package com.ak.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "personListEntityBuilder")
public class PersonListEntity {
    private final long id;
    private String name;
    private String surname;
    private Integer age;

    public static PersonListEntityBuilder builder(long id) {
        return personListEntityBuilder().id(id);
    }
}
