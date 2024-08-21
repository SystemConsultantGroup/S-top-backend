package com.scg.stop.global.infrastructure;

import com.scg.stop.global.domain.MailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class TransactionalEmailService implements EmailService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        applicationEventPublisher.publishEvent(new MailEvent(this, recipient, subject, body));
    }
}