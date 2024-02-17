package com.aimlab.service;

import com.aimlab.common.ErrorCode;
import com.aimlab.config.AppProperties;
import com.aimlab.dto.EmailVerificationDto;
import com.aimlab.entity.MailVerificationEntity;
import com.aimlab.exception.CustomException;
import com.aimlab.repository.MailVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

/**
 * MySql 이메일 인증 서비스
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MailVerificationServiceMysql implements MailVerificationService{
    private final MailVerificationRepository mailVerificationRepository;
    private final MailService mailService;
    private final AppProperties appProperties;

    @Override
    public String createVerification(String email) {
        long validityTime = appProperties.getMail().getVerificationValidityTime();
        long retentionTime = appProperties.getMail().getVerificationRetentionTime();

        // 1. 랜덤 인증 코드 생성
        int code = 100000 + new Random().nextInt(999999 - 100000 + 1);
        String verificationCode = String.valueOf(code);

        // 2. 인증 데이터 생성
        MailVerificationEntity mailVerification = MailVerificationEntity.builder()
                .email(email)
                .verificationCode(verificationCode)
                .expiredAt(LocalDateTime.now().plusMinutes(validityTime))
                .retentionAt(LocalDateTime.now().plusMinutes(retentionTime))
                .isConfirm(false).build();
        mailVerificationRepository.save(mailVerification);

        // 3. 이메일 전송
        mailService.sendVerificationMail(email, verificationCode, validityTime);

        return mailVerification.getKey().toString();
    }

    @Override
    public void confirmVerification(EmailVerificationDto emailVerificationDto) {
        UUID key = UUID.fromString(emailVerificationDto.getKey());
        String verificationCode = emailVerificationDto.getVerification_code();
        String email = emailVerificationDto.getUser_email();

        // 1. 인증 데이터 조회
        MailVerificationEntity mailVerification = mailVerificationRepository.findByKey(key)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_VERIFICATION_CODE));

        // 2. 인증 데이터 유효성 검사
        if (!mailVerification.getVerificationCode().equals(verificationCode)
        || !mailVerification.getEmail().equals(email)){
            throw new CustomException(ErrorCode.INVALID_VERIFICATION_CODE);
        } else if (mailVerification.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new CustomException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        // 3. 확인 완료
        mailVerification.setConfirm(true);
        mailVerificationRepository.save(mailVerification);
    }

    @Override
    public boolean checkConfirmedVerification(String key, String email) {
        // 1. 인증 데이터 조회
        MailVerificationEntity mailVerification = mailVerificationRepository.findByKey(UUID.fromString(key))
                .orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED_EMAIL));

        // 2. 유효성 검사
        if(mailVerification.getRetentionAt().isBefore(LocalDateTime.now())
        || !mailVerification.isConfirm()
        || !mailVerification.getEmail().equals(email)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_EMAIL);
        }

        // 3. 인증 데이터 삭제
        mailVerificationRepository.delete(mailVerification);
        return true;
    }
}
