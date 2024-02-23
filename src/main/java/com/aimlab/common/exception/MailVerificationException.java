package com.aimlab.common.exception;

public class MailVerificationException extends CustomException {

    public MailVerificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MailVerificationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
