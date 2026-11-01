package com.finsecure.payapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ErrorController {

    @GetMapping("/trigger-error")
    public String triggerError() {
        // Intentionally throw a runtime exception to generate an HTTP 500 Internal Server Error
        throw new RuntimeException("Deliberate 5xx error triggered for monitoring and alerting test");
    }
}