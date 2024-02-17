package com.aimlab.service;

import com.aimlab.dto.EmailVerificationDto;

/**
 * 이메일 인증 서비스
 */
public interface MailVerificationService {
    /**
     * 인증 데이터 생성
     * @param email
     * @return 인증 데이터 키
     */
    String createVerification(String email);

    /**
     * 인증 확인
     * @param emailVerificationDto 이메일 인증 DTO
     */
    void confirmVerification(EmailVerificationDto emailVerificationDto);

    /**
     * 인증 완료된 데이터 확인
     * @param key
     */
    boolean checkConfirmedVerification(String key, String email);
}
