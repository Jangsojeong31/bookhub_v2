package com.bookhub.bookhub_back.exception;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

public class ReferencedEntityException extends BusinessException{

    public ReferencedEntityException() {
        super(ResponseCode.ENTITY_REFERENCE, ResponseMessageKorean.ENTITY_REFERENCE);
    }

    public ReferencedEntityException(String message) {
        super(ResponseCode.ENTITY_REFERENCE, message);
    }
}
