package com.aimlab.service;

import com.aimlab.common.exception.ErrorCode;
import com.aimlab.common.exception.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    /**
     * 일반 메일 전송
     * @param toEmail 보낼 이메일 주소
     * @param subject 이메일 제목
     * @param text 이메일 내용(html)
     */
    public void sendMail(String toEmail, String subject, String text){
        try{
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);
        } catch (MailException | MessagingException exception){
            throw new CustomException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    /**
     * 인증코드 발급 이메일 전송
     * @param toEmail 보낼 이메일 주소
     * @param verificationCode 6자리 숫자 인증코드
     * @param validityTime 만료 시간
     */
    public void sendVerificationMail(String toEmail, String verificationCode, long validityTime){
        String subject = "AimSharp 회원가입 인증코드 안내";

        Context context = new Context();
        context.setVariable("code", verificationCode);
        context.setVariable("validityTime", validityTime);
        String text = templateEngine.process("verificationMail", context);

        try{
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);

            // 이미지 첨부
            Path imagePath = Paths.get(getClass().getResource("/static/logo/aimsharp_logo1.png").toURI());
            byte[] bytes = Files.readAllBytes(imagePath);
            helper.addInline("logo", new ByteArrayResource(bytes), "image/jpeg");

            emailSender.send(message);
        } catch (IOException | MessagingException | URISyntaxException exception){
            throw new CustomException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

}
