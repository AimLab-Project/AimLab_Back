package com.aimlab.exception;

import com.aimlab.common.ErrorCode;

public class MailVerificationException extends CustomException{

    public MailVerificationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MailVerificationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
