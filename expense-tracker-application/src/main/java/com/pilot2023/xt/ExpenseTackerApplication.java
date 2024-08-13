package com.pilot2023.xt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.pilot2023.xt"})
public class ExpenseTackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpenseTackerApplication.class, args);
    }

}