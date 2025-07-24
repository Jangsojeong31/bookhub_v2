package com.bookhub.bookhub_back.exception;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

public class AuthenticationException extends BusinessException{

    public AuthenticationException() {
        super(ResponseCode.SIGN_IN_FAIL, ResponseMessageKorean.SIGN_IN_FAIL);
    }

    public AuthenticationException(String message) {
        super(ResponseCode.SIGN_IN_FAIL, message);
    }
}
