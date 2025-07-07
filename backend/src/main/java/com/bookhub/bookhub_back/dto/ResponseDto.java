package com.bookhub.bookhub_back.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {

    private String code;     // 성공/실패 상태 코드
    private String message;  // 상태 메시지
    private T data;          // 응답 데이터

    // 성공 응답 (데이터 포함)
    public static <T> ResponseDto<T> success(String code, String message, T data) {
        return new ResponseDto<>(code, message, data);
    }

    // 성공 응답 (데이터 없음)
    public static <T> ResponseDto<T> success(String code, String message) {
        return new ResponseDto<>(code, message, null);
    }

    // 실패 응답 (제네릭)
    public static <T> ResponseDto<T> fail(String code, String message) {
        return new ResponseDto<>(code, message, null);
    }

    // 실패 응답을 ResponseEntity로 감싸서 반환
    public static ResponseEntity<ResponseDto<?>> failWithStatus(String code, String message, HttpStatus status) {
        ResponseDto<?> response = new ResponseDto<>(code, message, null);
        return ResponseEntity.status(status).body(response);
    }

    // ResponseEntity 매핑
    public static <T> ResponseEntity<ResponseDto<T>> toResponseEntity(HttpStatus status, ResponseDto<T> body) {
        return ResponseEntity.status(status).body(body);
    }
}
