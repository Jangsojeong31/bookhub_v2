## ❗ exception 패키지

`exception` 패키지는 **전역 예외 처리(Global Exception Handling)**를 담당합니다.  
애플리케이션 전반에서 발생할 수 있는 다양한 예외 상황을 일관된 방식으로 처리하여, 클라이언트에 명확한 응답을 제공합니다.

---

### 📄 GlobalExceptionHandler

`@RestControllerAdvice`를 사용해 **모든 Controller의 예외를 한 곳에서 처리**합니다.

#### ✅ 주요 역할
- 예외 상황에 따른 커스텀 메시지 및 상태 코드 반환
- 공통 응답 포맷(`ResponseDto`) 사용
- 예외 로그 출력으로 디버깅 용이성 향상

#### ✅ 주요 처리 예외

| 예외 타입 | HTTP 상태 코드 | 설명 |
|----------|----------------|------|
| `IllegalArgumentException`, `IllegalStateException` | 400 Bad Request | 잘못된 요청 |
| `MethodArgumentNotValidException` | 400 Bad Request | DTO 유효성 검사 실패 |
| `AccessDeniedException` | 403 Forbidden | 권한 없음 |
| `EntityNotFoundException` | 404 Not Found | 리소스 없음 |
| `DataIntegrityViolationException` | 409 Conflict | DB 제약 조건 위반 |
| `Exception` | 500 Internal Server Error | 기타 예상치 못한 예외 |

#### ✅ 응답 포맷 예시 (`ResponseDto`)
```json
{
  "code": "VALIDATION_FAIL",
  "message": "이메일 형식이 올바르지 않습니다.",
  "data": null
}
