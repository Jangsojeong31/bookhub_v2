package com.bookhub.bookhub_back.exception;

// 중복되는 값 입력 시 발생하는 예외 (unique 위반)
public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
