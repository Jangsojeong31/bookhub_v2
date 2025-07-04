# 🔧 config 패키지

`config` 패키지는 애플리케이션의 전역 설정을 담당합니다. 

외부 서비스 연동, 보안 설정, 정적 리소스 처리 등을 구성합니다.

- `MailConfig` : SMTP 설정을 통한 이메일 전송 구성
- `WebFileConfig` : 업로드 파일 경로 및 정적 자원 URL 매핑 설정
- `WebSecurityConfig` : Spring Security 기반 인증/인가, JWT 필터, CORS 정책 구성
