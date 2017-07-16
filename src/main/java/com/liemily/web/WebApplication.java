package com.liemily.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * WebApplication Spring Boot starter
 * Created by Emily Li on 16/07/2017.
 */
@RestController
@SpringBootApplication
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/secure")
    String secure() {
        return "This is secure!";
    }
}
