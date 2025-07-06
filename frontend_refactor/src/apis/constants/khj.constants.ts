const API_DOMAIN = "http://localhost:8080";

const ADMIN = "admin";
const MANAGER = "manager";
const COMMON = "common";

//! 1) 인증 관련 요청 URL
const AUTH_MODULE_URL = `${API_DOMAIN}/api/v1/auth`;

//? 인증 관련 기능
// 1) 회원 가입
export const SIGN_UP_URL = `${AUTH_MODULE_URL}/signup`;

// 2) 로그인
export const SIGN_IN_URL = `${AUTH_MODULE_URL}/login`;

// 3) 아이디 찾기 - 이메일 보내기
export const LOGIN_ID_FIND_EMAIL_URL = `${AUTH_MODULE_URL}/login-id-find/email`;

// 3) 아이디 찾기
export const LOGIN_ID_FIND_URL = `${AUTH_MODULE_URL}/login-id-find`;

// 4) 비밀번호 변경 - 이메일 보내기
export const PASSWORD_CHANGE_EMAIL_URL = `${AUTH_MODULE_URL}/password-change/email`;

// 4) 비밀번호 변경
export const PASSWORD_CHANGE_URL = `${AUTH_MODULE_URL}/password-change`;

// 5) 아이디 중복 확인
export const CHECK_LOGIN_ID_DUPLICATE = `${AUTH_MODULE_URL}/login-id-exists`;

// 6) 로그아웃
export const LOGOUT_URL = `${AUTH_MODULE_URL}/logout`;

// 7) 회원가입 승인 알림 이메일
export const SIGN_UP_RESULT_URL = (approvalId: number) =>
  `${AUTH_MODULE_URL}/employees/${approvalId}/approve`;

// 8) 회원가입 승인 수정
export const EMPLOYEE_UPDATE_URL = `${AUTH_MODULE_URL}/employees/approve`;

//! 2) ADMIN 관련 요청 URL
const ADMIN_MODULE_URL = `${API_DOMAIN}/api/v1/${ADMIN}`;

//? 지점 관련 기능
const BRANCH_MODULE_URL = `${ADMIN_MODULE_URL}/branches`;

// 1) 지점 등록
export const POST_BRANCH_URL = `${BRANCH_MODULE_URL}`;

// 2) 지점 조회
export const GET_BRANCH_URL = `${AUTH_MODULE_URL}/branches`;

// 3) 단건 조회
export const GET_BRANCH_DETAIL_URL = (branchId: number) =>
  `${BRANCH_MODULE_URL}/${branchId}`;

// 4) 지점 수정
export const PUT_BRANCH_URL = (branchId: number) =>
  `${BRANCH_MODULE_URL}/${branchId}`;

// 5) 지점 삭제
export const DELETE_BRANCH_URL = (branchId: number) =>
  `${BRANCH_MODULE_URL}/${branchId}`;

//? 직원 관련 기능
const EMPLOYEE_MODULE_URL = `${ADMIN_MODULE_URL}/employees`;

// 1) 직원 전체 조회
export const GET_ALL_EMPLOYEE_URL = `${EMPLOYEE_MODULE_URL}`;

// 2) 직원 단건 조회
export const GET_EMPLOYEE_URL = (employeeId: number) =>
  `${EMPLOYEE_MODULE_URL}/${employeeId}`;

// 3) 직원 정보 수정
export const PUT_EMPLOYEE_CHANGE_URL = (employeeId: number) =>
  `${EMPLOYEE_MODULE_URL}/${employeeId}/organization`;

// 4) 직원 퇴직 여부 수정
export const PUT_EMPLOYEE_STATUS_URL = (employeeId: number) =>
  `${EMPLOYEE_MODULE_URL}/${employeeId}/status`;

// 5) 회원 가입 신청 직원 조회
export const GET_PENDING_EMPLOYEE_URL = `${EMPLOYEE_MODULE_URL}/approval`;

// 6) 직원 회원가입 승인
export const PUT_EMPLOYEE_APPROVE_URL = (employeeId: number) =>
  `${EMPLOYEE_MODULE_URL}/${employeeId}/approve`;

//? 로그 관련 기능
// 직원 수정 URL
const EMPLOYEE_CHANGE_LOGS_MODULE_URL = `${ADMIN_MODULE_URL}/employee-change-logs`;

// 퇴사자 URL
const EMPLOYEE_SIGN_UP_APRROVALS_MODULE_URL = `${ADMIN_MODULE_URL}/employee-signup-approvals`;

// 회원가입 승인 URL
const EMPLOYEE_EXIT_LOGS_MODULE_URL = `${ADMIN_MODULE_URL}/employee-exit-logs`;

// 1) 지점, 직급, 권한 변경 로그
export const GET_ALL_EMPLOYEE_CHANGE_LOGS_URL = `${EMPLOYEE_CHANGE_LOGS_MODULE_URL}`;

// 2) 회원가입 승인 로그
export const GET_ALL_EMPLOYEE_SIGN_UP_APRROVALS_URL = `${EMPLOYEE_SIGN_UP_APRROVALS_MODULE_URL}`;

// 3) 직원 퇴사자 로그
export const GET_ALL_EMPLOYEE_EXIT_LOGS_URL = `${EMPLOYEE_EXIT_LOGS_MODULE_URL}`;


export const GET_ALL_POSITION_URL = `${AUTH_MODULE_URL}/positions`;

export const GET_ALL_AUTHORITY_URL = `${AUTH_MODULE_URL}/authorities`;