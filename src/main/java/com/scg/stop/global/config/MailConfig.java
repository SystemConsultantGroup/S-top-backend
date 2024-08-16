package com.scg.stop.global.config;

import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.global.infrastructure.TransactionalEmailService;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@RequiredArgsConstructor
public class MailConfig {
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_DEBUG = "mail.smtp.debug";
    private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

    // SMTP 서버
    @Value("${spring.mail.host}")
    private String host;

    // 계정
    @Value("${spring.mail.username}")
    private String username;

    // 비밀번호
    @Value("${spring.mail.password}")
    private String password;

    // 포트번호
    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.mail.smtp.debug}")
    private boolean debug;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private boolean startTlsEnable;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put(MAIL_SMTP_AUTH, auth);
        properties.put(MAIL_DEBUG, debug);
        properties.put(MAIL_CONNECTION_TIMEOUT, connectionTimeout);
        properties.put(MAIL_SMTP_STARTTLS_ENABLE, startTlsEnable);

        javaMailSender.setJavaMailProperties(properties);
        javaMailSender.setDefaultEncoding("UTF-8");

        return javaMailSender;
    }


}
