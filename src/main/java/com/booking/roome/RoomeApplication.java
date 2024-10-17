package com.booking.roome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class RoomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RoomeApplication.class, args);
    }

}
