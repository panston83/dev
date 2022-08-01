package com.example.dev.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class reactController {
    @GetMapping("main")
    public List<String> main() {
        System.out.println("테스트 한글 테스트");
        System.out.println("실시간 테스트 테스트");
        System.out.println("대체 왜 깨지지");
        System.out.println("맥이어서 그런가");
        System.out.println("오호 그렇다면 여기도?");
        return Arrays.asList("으르렁 테스트", "Hello");
    }
}