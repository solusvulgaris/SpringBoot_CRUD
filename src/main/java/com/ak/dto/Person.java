package com.ak.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * API model shared by the REST layer and application services.
 *
 * <p>The persistence representation is intentionally kept separate in
 * {@code PersonJpaEntity}; conversion between the two models belongs in a
 * mapper.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private Long id;
    private String name;
    private String surname;
    private Integer age;
    private LocalDateTime createdAt;
}
