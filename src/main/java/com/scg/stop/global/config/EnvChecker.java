package com.scg.stop.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class EnvChecker implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String dbHost;

    @Override
    public void run(String... args) {
        System.out.println("DB_HOST = " + dbHost);
    }
}
