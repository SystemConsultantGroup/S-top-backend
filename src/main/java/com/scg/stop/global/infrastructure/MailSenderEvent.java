package com.scg.stop.global.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailSenderEvent {
    private String email;
}
