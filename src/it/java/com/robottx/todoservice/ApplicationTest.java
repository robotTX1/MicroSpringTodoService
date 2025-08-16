package com.robottx.todoservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplicationTest extends IntegrationTest {

    @Autowired
    ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }

}
