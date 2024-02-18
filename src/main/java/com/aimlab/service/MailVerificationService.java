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
     * @param key 인증키
     * @param email 이메일
     * @param verificationCode 인증코드
     */
    void confirmVerification(String key, String email, String verificationCode);

    /**
     * 인증 완료된 데이터 확인
     * @param key 인증키
     */
    boolean checkConfirmedVerification(String key, String email);
}
