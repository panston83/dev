package com.example.dev.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class reactController {
    @GetMapping("main")
    public List<String> main() {
        return Arrays.asList("안녕하세요", "Hello");
    }
}
