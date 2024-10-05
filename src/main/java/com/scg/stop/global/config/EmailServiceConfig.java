package com.scg.stop.global.config;

import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.global.infrastructure.TransactionalEmailService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailServiceConfig {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EmailServiceConfig(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Bean
    public EmailService emailService() {
        return new TransactionalEmailService(applicationEventPublisher);
    }
}