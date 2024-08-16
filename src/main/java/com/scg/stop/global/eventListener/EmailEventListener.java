package com.scg.stop.global.eventListener;


import com.scg.stop.proposal.domain.ProposalEvent;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Component
@Slf4j
public class EmailEventListener {

    private final JavaMailSender mailSender;

    @Async("mailExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void listen(ProposalEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(event.getRecipient());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), true);
            mailSender.send(message);
        } catch (Exception e) {
//            e.printStackTrace();
            log.debug("TransactionalEmailSend Error: {}", e.toString());
        }
    }
}
