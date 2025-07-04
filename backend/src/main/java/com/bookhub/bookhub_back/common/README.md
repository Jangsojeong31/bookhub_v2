# 📁 common 패키지

전역적으로 사용하는 상수, 공통 규칙, 유틸리티용 enum/상수 정의

---

## ✅ constants 패키지

전역적으로 사용되는 상수(Constant)들을 모아두는 패키지

### 사용 예시
| 파일명                      | 설명 |
|--------------------------|----|
| `ApiMappingPattern.java` | -- |
| `ResponseCode.java`      | -- |
| `ResponseMessage.java`   | -- |


## ✅ utils 패키지

전역적으로 활용 가능한 유틸리티 메서드(Method)들을 모아두는 패키지

### 사용 예시
| 파일명             | 설명 |
|------------------|------|
| `DateUtil.java` | LocalDate, Date, Timestamp 간 변환, 날짜 포맷팅 등 |
| `StringUtil.java` | null 또는 빈 문자열 체크, 문자열 포맷팅, 마스킹 등 |
| `EmailUtil.java` | 이메일 형식 검증, 인증 코드 생성 |
| `PasswordUtil.java` | 비밀번호 암호화/검증 (BCrypt 등) |

## 📌 폴더 구조 예시

    common
    └── constants
        ├── ApiMappingPattern.java
        ├── ResponseMessage.java
        ├── ResponseCode.java
        ├── RoleType.java
        ├── DateFormatPattern.java
        ├── ValidationMessage.java
        └── ApiVersion.java