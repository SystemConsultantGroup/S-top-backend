package com.scg.stop.global.EventListener;

import com.scg.stop.global.infrastructure.MailSenderEvent;
import com.scg.stop.proposal.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class EmailEventListener {

    private final ProposalService proposalService;
    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MailSenderEvent.class)
    public void handle(MailSenderEvent event) {

    }
}
