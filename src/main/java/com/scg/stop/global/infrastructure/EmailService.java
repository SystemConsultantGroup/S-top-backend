package com.scg.stop.global.infrastructure;


public interface EmailService {
    void sendEmail(String recipient, String subject, String body);
}
