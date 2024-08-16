package com.scg.stop.global.infrastructure;

import com.scg.stop.proposal.domain.ProposalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;

@RequiredArgsConstructor
public class TransactionalEmailService implements EmailService {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void sendEmail(String recipient, String subject, String body) {
        applicationEventPublisher.publishEvent(new ProposalEvent(this, recipient, subject, body));
    }
}
