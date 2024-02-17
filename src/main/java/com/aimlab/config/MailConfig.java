package com.aimlab.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String mailServerHost;

    @Value("${spring.mail.port}")
    private Integer mailServerPort;

    @Value("${spring.mail.username}")
    private String mailServerUsername;

    @Value("${spring.mail.password}")
    private String mailServerPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailServerAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailServerStartTlsEnable;

    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String mailServerStartTlsRequired;

    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String mailServerTimeout;

    @Value("${spring.mail.properties.mail.smtp.connectiontimeout}")
    private String mailServerConnectionTimeout;

    @Value("${spring.mail.properties.mail.smtp.writetimeout}")
    private String mailServerWriteTimeout;

    @Bean
    public JavaMailSender javaMailSender(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(mailServerHost);
        javaMailSender.setPort(mailServerPort);
        javaMailSender.setDefaultEncoding("UTF-8");

        javaMailSender.setUsername(mailServerUsername);
        javaMailSender.setPassword(mailServerPassword);

        Properties properties = javaMailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", mailServerAuth);
        properties.put("mail.smtp.starttls.enable", mailServerStartTlsEnable);
        properties.put("mail.smtp.starttls.required", mailServerStartTlsRequired);
        properties.put("mail.smtp.timeout", mailServerTimeout);
        properties.put("mail.smtp.connectiontimeout", mailServerConnectionTimeout);
        properties.put("mail.smtp.writetimeout", mailServerWriteTimeout);
        properties.put("mail.debug", "true");

        return javaMailSender;
    }
}
