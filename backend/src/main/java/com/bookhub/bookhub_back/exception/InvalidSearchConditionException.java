package com.bookhub.bookhub_back.exception;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

public class InvalidSearchConditionException extends BusinessException{

    public InvalidSearchConditionException() {
        super(ResponseCode.INVALID_SEARCH_CONDITION, ResponseMessageKorean.INVALID_SEARCH_CONDITION);
    }

    public InvalidSearchConditionException(String message) {
        super(ResponseCode.INVALID_SEARCH_CONDITION, message);
    }
}
