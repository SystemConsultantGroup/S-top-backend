package com.scg.stop.proposal.domain;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class MailEvent extends ApplicationEvent {
    private final String recipient;
    private final String subject;
    private final String body;

    public MailEvent(Object source, String recipient, String subject, String body) {
        super(source);
        this.recipient = recipient;
        this.subject = subject;
        this.body = body;
    }

}
