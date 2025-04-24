package com.frontice.gateway.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/user-service")
    public Mono<String> userServiceFallback() {
        return Mono.just("User Service is currently unavailable. Please try again later.");
    }

    @RequestMapping("/fallback/product-service")
    public Mono<String> productServiceFallback() {
        return Mono.just("Product Service is currently unavailable. Please try again later.");
    }

    @RequestMapping("/fallback/order-service")
    public Mono<String> orderServiceFallback() {
        return Mono.just("Order Service is currently unavailable. Please try again later.");
    }
}