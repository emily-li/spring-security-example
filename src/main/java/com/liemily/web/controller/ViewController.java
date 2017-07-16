package com.liemily.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Emily Li on 16/07/2017.
 */
@RestController
public class ViewController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @RequestMapping("/secure")
    String secure() {
        return "This is secure!";
    }
}
