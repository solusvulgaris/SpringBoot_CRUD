package com.ak;

import com.ak.web.v1.PersonController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PersonApplicationTest {
    @Autowired
    private PersonController controller;

    @Test
    void Given_Exists_When_ContextLoads_Then_IsNotNull() {
        Assertions.assertThat(controller).isNotNull();
    }
}
