package com.scg.stop.project.domain;

import org.springframework.context.ApplicationEvent;

public class InquiryEvent extends ApplicationEvent {

    private final String recipient;
    private final String subject;
    private final String body;

    public InquiryEvent(Object source, String recipient, String subject, String body) {
        super(source);
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }
}
