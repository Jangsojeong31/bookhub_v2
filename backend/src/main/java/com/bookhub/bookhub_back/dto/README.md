## 📦 dto 패키지

`dto`(Data Transfer Object) 패키지는 **Controller ↔ Service 간 데이터 전달**을 책임집니다.  
입력(Request)과 출력(Response)을 명확히 분리하여 계층 간 책임을 구분하고 유지보수를 용이하게 합니다.

---

### 📂 request

`request` 하위 패키지는 **클라이언트로부터 전달받는 데이터 구조**를 정의합니다.

#### ✅ 사용 어노테이션

- `@Getter` : 필드에 대한 Getter 자동 생성
- `@NoArgsConstructor` : 기본 생성자 생성
- `@Valid` : 컨트롤러에서 해당 객체 유효성 검사 적용
- 필드 단위 유효성 검사 어노테이션:
    - `@NotNull` : null 금지 (객체 타입)
    - `@NotBlank` : 빈 문자열 금지 (문자열 타입)
    - `@Email` : 이메일 형식 검사
    - `@Pattern` : 정규식 패턴 검사

#### ⛔ 필수 어노테이션
- 클래스: `@Getter`, `@NoArgsConstructor`
- 유효성 검사 목적: `@Valid` (컨트롤러 메서드에서 적용)
- 필드: `@NotNull`, `@NotBlank`, `@Email`, `@Pattern` 등 필요에 따라 적용

---

### 📂 response

`response` 하위 패키지는 **서비스 처리 결과를 클라이언트로 반환하는 구조**를 정의합니다.

#### ✅ 사용 어노테이션

- `@Getter` : 필드에 대한 Getter 자동 생성
- `@AllArgsConstructor` : 전체 필드 초기화 생성자
- `@Builder` *(선택)* : 유연한 객체 생성 패턴 제공

#### ⛔ 필수 어노테이션
- 클래스: `@Getter`, `@AllArgsConstructor`
- `@Builder`는 선택이지만, 가독성 및 유지보수 측면에서 추천

---

### 📄 ResponseDto<T>

`ResponseDto`는 **모든 API 응답을 통일된 구조**로 감싸기 위한 제네릭 래퍼입니다.

#### ✅ 주요 필드

| 필드명   | 설명                         |
|----------|------------------------------|
| `code`   | 응답 코드 (`"SUCCESS"`, `"ERROR"`, 등) |
| `message` | 사용자에게 전달할 메시지          |
| `data`   | 실제 응답 데이터 (`T` 타입 제네릭) |

#### ✅ 주요 메서드

- `success(code, message, data)`  
  → 데이터 포함 성공 응답

- `success(code, message)`  
  → 데이터 없는 성공 응답

- `fail(code, message, status)`  
  → 실패 응답 반환 (`HttpStatus` 명시)

#### ✅ 장점
- 모든 API 응답 형식을 통일하여 클라이언트에서 일관된 처리 가능
- 성공과 실패 응답을 명확하게 분리
- 제네릭을 활용하여 어떤 타입의 응답도 처리 가능

---

### ✅ 예시 구조

```java
// 요청 DTO 예시
@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    
    @NotBlank
    private String username;

    @Email
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$")
    private String password;
}

// 응답 DTO 예시
@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private String userId;
    private String username;
}
