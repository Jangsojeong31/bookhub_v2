package com.bookhub.bookhub_back.exception;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;

public class DuplicateEntityException extends BusinessException{

    public DuplicateEntityException() {
        super(ResponseCode.DUPLICATED_ENTITY, ResponseMessageKorean.DUPLICATED_ENTITY);
    }

    public DuplicateEntityException(String message) {
        super(ResponseCode.DUPLICATED_ENTITY, message);
    }


}
