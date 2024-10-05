package com.scg.stop.global.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmailEvent extends ApplicationEvent {

    private final String recipient;
    private final String subject;
    private final String body;

    public EmailEvent(Object source, String recipient, String subject, String body) {
        super(source);
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }
}