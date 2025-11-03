package com.example.cepsacbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableCaching
@EnableAsync // proceso asincrono
public class CepsacBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(CepsacBackendApplication.class, args);
    }

}
