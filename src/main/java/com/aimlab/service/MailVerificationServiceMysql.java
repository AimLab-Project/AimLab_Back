package com.aimlab.service;

import com.aimlab.common.ErrorCode;
import com.aimlab.config.AppProperties;
import com.aimlab.entity.MailVerificationEntity;
import com.aimlab.exception.CustomException;
import com.aimlab.exception.MailVerificationException;
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
                .expiresAt(LocalDateTime.now().plusMinutes(validityTime))
                .retentionAt(LocalDateTime.now().plusMinutes(retentionTime))
                .isConfirm(false)
                .attemptCount(0).build();
        mailVerificationRepository.save(mailVerification);

        // 3. 이메일 전송
        mailService.sendVerificationMail(email, verificationCode, validityTime);

        return mailVerification.getKey().toString();
    }

    @Override
    @Transactional(noRollbackFor = MailVerificationException.class)
    public void confirmVerification(String key, String email, String verificationCode) {
        // 1. 인증 데이터 조회
        MailVerificationEntity mailVerification = mailVerificationRepository.findByKey(UUID.fromString(key))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VERIFICATION));

        // 2. 인증 데이터 유효성 검사
        // 2-1. 이메일 인증 초과 횟수 여부
        if (mailVerification.getAttemptCount() > appProperties.getMail().getMaxAuthenticationAttempts()){
            throw new MailVerificationException(ErrorCode.VERIFICATION_ATTEMPTS_EXCEEDED);
        }

        // 2-2. 인증 만료 여부
        if(mailVerification.getExpiresAt().isBefore(LocalDateTime.now())){
            throw new MailVerificationException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        // 2-3. 인증 번호 or 이메일 불일치 => 인증 횟수 증가
        if(!mailVerification.getVerificationCode().equals(verificationCode) || !mailVerification.getEmail().equals(email)){
            mailVerification.setAttemptCount(mailVerification.getAttemptCount() + 1);
            mailVerificationRepository.save(mailVerification);
            throw new MailVerificationException(ErrorCode.INVALID_VERIFICATION_CODE);
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
