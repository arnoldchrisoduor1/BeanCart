package com.yourcompany.app.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/api")
public class HelloWorldController {

    private final FF4j ff4j;

    @Autowired
    public HelloWorldController(FF4j ff4j) {
        this.ff4j = ff4j;
        
        // Create a hello world feature flag if it doesn't exist
        if (!ff4j.exist("hello-world-feature")) {
            ff4j.createFeature(new Feature("hello-world-feature", true));
        }
    }
    
    @GetMapping("/hello")
    @PermitAll
    public ResponseEntity<String> helloWorld() {
        if (ff4j.check("hello-world-feature")) {
            return ResponseEntity.ok("Hello, World! Your Spring Boot application is running successfully!");
        } else {
            return ResponseEntity.ok("Feature is currently disabled.");
        }
    }
}