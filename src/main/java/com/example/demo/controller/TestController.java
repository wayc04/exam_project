package com.example.demo.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class TestController {
    @RequestMapping("/test")
    public String test() {
        return "Hello World!";
    }
    // 测试返回json格式的response数据
    @GetMapping("/test2")
    public ResponseEntity<Map> test2() {
        Map<String, String> map = Map.of("message", "Hello World!");
        return ResponseEntity.ok().body(map);
//        return ResponseEntity.ok().body("Hello World!");
    }

}
