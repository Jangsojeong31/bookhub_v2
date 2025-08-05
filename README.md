# BookHub_v2
<div align=center>
<img width="250" alt="북허브_로고" src="https://github.com/user-attachments/assets/51e523f3-9c60-4510-b88f-c05f7662bc71" />
<p align="center">
    <strong>개발 기간</strong> : 2025.04 ~ 2025.07 <br/>
    <strong>배포 주소</strong> : <a href="http://3.35.24.241/">http://3.35.24.241/</a>
</p>

본 프로젝트는 코리아IT아카데미 국비 교육 과정 중 진행된 팀 프로젝트를 기반으로,<br/>
구조 개선을 위해 개인적으로 리팩토링한 버전입니다.
</div>

---


## 목차   
+ [프로젝트 소개](#-프로젝트-소개)
+ [화면 소개](#%EF%B8%8F-화면-소개)
+ [기술 스택](#%EF%B8%8F-기술-스택)
+ [개인 리팩토링](#-개인-리팩토링)
+ [설계 문서](#-설계-문서)
+ [팀원 소개](#-팀원-소개)


---


## 📌 프로젝트 소개

> 도서 관리와 유통 기능 중심의 ERP 시스템입니다.<br/>
> 권한 기반 접근 제어와 로그 기록을 통해 보안을 강화하고,<br/>
> 발주 → 수령 → 재고로 이어지는 흐름을 시스템화하여 효율적인 도서 유통과 관리를 지원합니다.

### ✨ 주요 기능
+ **회원가입 / 로그인**: JWT 기반 인증과 관리자 승인 절차
  
+ **권한을 통한 접근 제어**: STAFF/MANAGER/ADMIN 별 기능 제한
  
+ **사원 관리 / 도서 관리**: 권한 부여 및 도서 정보 등록/수정
  
+ **발주 / 수령 / 재고 관리**: 알림 기반 재고 파악 및 자동 재고 반영
  
+ **로그 기록 및 관리**: 모든 주요 이벤트를 기록하여 감사 및 보안 관리
  
+ **알림 기능**: 이벤트 발생 시 실시간 알림
  
+ **통계 차트**: 베스트셀러 및 매출 현황 시각화

---

## 🖥️ 화면 소개  


---

## 🛠️ 기술 스택
**Frontend**   
![React](https://img.shields.io/badge/React-DC1015?style=flat&logo=react&logoColor=white) 
![HTML%](https://img.shields.io/badge/HTML5-DB3A1E?style=flat&logo=html5&logoColor=white) 
![CSS](https://img.shields.io/badge/CSS-DB642C?style=flat&logo=css&logoColor=white) 
![TypeScript](https://img.shields.io/badge/TypeScript-DC8230?style=flat&logo=typescript&logoColor=white) 
![Zustand](https://img.shields.io/badge/Zustand-DBA340?style=flat&logoColor=white)

**Backend**  
![Java](https://img.shields.io/badge/Java-007396?style=flat&logo=openjdk&logoColor=white) 
![Spring Boot](https://img.shields.io/badge/Spring_Boot-2E70DB?style=flat&logo=springboot&logoColor=white) 
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-32A1EA?style=flat&logo=springdatajpa&logoColor=white) 
![Spring Security](https://img.shields.io/badge/Spring_Security-34B2DB?style=flat&logo=springsecurity&logoColor=white) 
![JWT](https://img.shields.io/badge/JWT-3DD0DC?style=flat&logoColor=white) 
![Spring Boot Mail](https://img.shields.io/badge/Spring_Boot_Mail-1C9CD2?style=flat)
![Spring Boot Validation](https://img.shields.io/badge/Spring_Boot_Validation-6DB33F?style=flat)
![Swagger](https://img.shields.io/badge/Swagger-A852E5?style=flat&logo=swagger&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-8B22DB?style=flat&logo=mysql&logoColor=white)

**협업**  
![GitHub](https://img.shields.io/badge/GtHub-B9E612?style=flat&logo=github&logoColor=white) 
![Notion](https://img.shields.io/badge/Notion-F7DF1E?style=flat&logo=notion&logoColor=white)

**Infra / DevOps**   
![AWS EC2](https://img.shields.io/badge/EC2-00B2A5?style=flat&logoColor=white) 
![RDS](https://img.shields.io/badge/RDS-897BFF?style=flat&logoColor=white) 
![Nginx](https://img.shields.io/badge/Nginx-87E59C?style=flat&logo=nginx&logoColor=white) 
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-FE5F50?style=flat&logo=githubactions&logoColor=white)

---

## 🔄 개인 리팩토링

### 개선 목표
- 백엔드 구조 개선 및 인증 강화
- 사용자 경험 향상
- API 명세 자동화
- 실전 배포 경험


### 주요 리팩토링 사항

+ **예외 처리 개선**
  + `@Valid`, 커스텀 예외 적용
  + `GlobalExceptionHandler`을 통한 응답 통일
    
+ **Spring Security 로그인 구조 개선**
  + `UserDetails`, `UserDetailsService` 구현
  + 비밀번호 암호화 및 표준 인증 흐름 구축
  + 권한 기반 접근 제어 로직 개선
    
+ **Swagger(OpenAPI) 도입**
  + 자동 API 문서화 및 테스트 지원
    
+ **배포 환경 구성**
  + EC2 + Nginx + RDS 환경 구성
  + GitHub Actions로 CI/CD 구성
    
+ **MyBatis 도입**
  + 복잡한 조건 검색 및 다중 조인을 XML SQL로 분리

---

## 📃 설계 문서
### 1. ERD
<img width="1519" height="1301" alt="북허브_erd" src="https://github.com/user-attachments/assets/269dc9ef-d6e7-4506-b880-38b640d3f772" />

### 2. API 명세서
<img width="994" height="1513" alt="A4 - 1" src="https://github.com/user-attachments/assets/dd0212a1-96b7-4428-9a5b-c49bbcfa26d3" />
<img width="994" height="2118" alt="A4 - 2" src="https://github.com/user-attachments/assets/aa196eb5-3b84-47dc-bcfd-543d513db2e3" />

---

## 🙏 팀원 소개

| 이름       | 고혁재 | 성재원 | 장소정 | 최서윤 |
|------------|--------|--------|--------|--------|
| 역할       | 백/프론트 | 백/프론트 | 백/프론트 | 백/프론트 |
| 담당 기능  | 로그인,<br/>JWT 토큰 인증,<br/>권한 제어,<br/>사원 관리, <br/>이메일 전송 기능 | 도서 관리<br/>(도서 CRUD, 도서 로그 기록), <br/>수령 확인, <br/>알림 기능, 메인화면 구성 | 작가 CRUD, <br/>발주 및 발주 승인, <br/>베스트셀러, 매출 통계, <br/>리팩토링, 배포 | 출판사 CRUD, <br/>재고 관리<br/>(재고 변경, 재고 로그 기록), <br/>매출 통계, <br/>헤더 및 사이드바 구현 |

