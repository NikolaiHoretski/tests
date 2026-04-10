package com.nikolaihoretski.tests;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestsApplication.class, args);
    }

}
