// csy.constants.ts
// # URL 상수 정의

const API_DOMAIN = import.meta.env.REACT_APP_API_DOMAIN || "http://localhost:8080";

const ADMIN = 'admin';
const MANAGER = 'manager';
const COMMON = 'common';

// & 1. publishers
// 베이스 URL
const PUBLISHER_MODULE_URL = `${API_DOMAIN}/api/v1/${ADMIN}/publishers`;
//const PUBLISHER_MODULE_URL = `${API_DOMAIN}/api/v1/publishers`;

// 1) 출판사 등록
export const POST_PUBLISHER_URL = `${PUBLISHER_MODULE_URL}`;

// 2) 출판사 전체 조회
export const GET_ALL_PUBLISHER_URL = `${PUBLISHER_MODULE_URL}`;

// 4) 출판사 수정
export const PUT_PUBLISHER_URL = (publisherId: number) => `${PUBLISHER_MODULE_URL}/${publisherId}`;

// 5) 출판사 삭제
export const DELETE_PUBLISHER_URL = (publisherId: number) => `${PUBLISHER_MODULE_URL}/${publisherId}`;

// & 2. locations

// 공통·관리자 공통 도메인
const LOCATION_BRANCH_MANAGER = `${API_DOMAIN}/api/v1/${MANAGER}/locations`
const LOCATION_BRANCH_COMMON  = `${API_DOMAIN}/api/v1/${COMMON}/locations`

// 1) 지점별 진열위치 등록 (POST)
export const POST_LOCATION_URL = 
  `${LOCATION_BRANCH_MANAGER}`

// 2) 지점별 진열위치 전체 조회 (GET 리스트)
export const GET_ALL_LOCATIONS_URL = 
  `${LOCATION_BRANCH_COMMON}`

// 3) 지점별 진열위치 단건 조회 (GET 상세)
export const GET_LOCATION_URL = ( locationId: number) =>
  `${LOCATION_BRANCH_COMMON}/${locationId}`

// 4) 지점별 진열위치 수정 (PUT)
export const PUT_LOCATION_URL = ( locationId: number) =>
  `${LOCATION_BRANCH_MANAGER}/${locationId}`

// 5) 지점별 진열위치 삭제 (DELETE)
export const DELETE_LOCATION_URL = ( locationId: number) =>
  `${LOCATION_BRANCH_MANAGER}/${locationId}`



// & 3. discount_policies
// 베이스 URL
const POLICY_URL_ADMIN = `${API_DOMAIN}/api/v1/${ADMIN}/policies`;
const POLICY_URL_COMMON = `${API_DOMAIN}/api/v1/${COMMON}/policies`

// 1) 이벤트 생성
export const POST_POLICY_URL = `${POLICY_URL_ADMIN}`;

//2) 이벤트 전체 조회
export const GET_ALL_POLICIES_URL = `${POLICY_URL_COMMON}`;

//3) 이벤트 단건 조회
export const GET_POLICY_URL = (PolicyId : number) => `${POLICY_URL_COMMON}/${PolicyId}`;

//4) 이벤트 수정
export const PUT_POLICY_URL = (PolicyId : number) => `${POLICY_URL_ADMIN}/${PolicyId}`;

//5) 이벤트 삭제
export const DELETE_POLICY_URL = (PolicyId : number) => `${POLICY_URL_ADMIN}/${PolicyId}`;

//&Stock
//1> Stock base Url
const STOCK_BASE_URL =  `${API_DOMAIN}/api/v1/${MANAGER}/stocks`;
//Stock update Url
//export const UPDATE_STOCK_URL = (branchId : number, stockId : number) => `${STOCK_BASE_URL}/branch/${branchId}/${stockId}`;
export const UPDATE_STOCK_URL = ( stockId : number) => `${STOCK_BASE_URL}/${stockId}`;

// 재고 조회
export const STOCK_SEARCH_BOOK_URL = (
  bookTitle: string,
  isbn : string,
  branchName: string
) => {
  const queryParams = new URLSearchParams();
  
  if (bookTitle) queryParams.append("bookTitle", bookTitle);
  if (isbn) queryParams.append("isbn", isbn);
  if (branchName) queryParams.append("branchName", branchName);

  return `${STOCK_BASE_URL}?${queryParams.toString()}`;
}

//&StockLog
export const STOCK_LOG_BASE_URL =  `${API_DOMAIN}/api/v1/${ADMIN}/stock-logs`;

// 지점별 재고 로그 조회
export const STOCK_LOGS_BY_BRANCH = (
  branchId: number,
  type: string,
  bookIsbn: string,
  start: string,
  end: string
) => {
  const queryParams = new URLSearchParams();
  
  if (type) queryParams.append("type", type);
  if (bookIsbn) queryParams.append("bookIsbn", bookIsbn);
  if (start) queryParams.append("start", start);
  if (end) queryParams.append("end", end);

  return `${STOCK_LOG_BASE_URL}/branch/${branchId}?${queryParams.toString()}`;
}

// 담당 사원별 재고 로그 조회
export const STOCK_LOGS_BY_EMPLOYEE = (employeeId: number) =>
  `${STOCK_LOG_BASE_URL}/employee/${employeeId}`;

// & 그 외
// 통계 베이스 URL
const STATISTICS_BASE_URL_ADMIN = `${API_DOMAIN}/api/v1/${ADMIN}/statistics`;

//*매출(Revenue) 통계 URL
export const REVENUE_STATISTICS_BASE_URL = `${STATISTICS_BASE_URL_ADMIN}/revenue`

//1) 요일별 매출 통계(전체)
export const REVENUE_STATISTICS_WEEKDAY_URL = `${REVENUE_STATISTICS_BASE_URL}/weekday`
//2) 주간 매출 통계
export const REVENUE_STATISTICS_WEEKLY_URL = `${REVENUE_STATISTICS_BASE_URL}/weekly`
//3) 월별 매출 통계 
export const REVENUE_STATISTICS_MONTHLY_URL = `${REVENUE_STATISTICS_BASE_URL}/monthly`
//4) 지점별 매출 통계
export const REVENUE_STATISTICS_BRANCH_URL = `${REVENUE_STATISTICS_BASE_URL}/branch`

//*Stock(Stock) 통계 URL
export const STOCK_STATISTICS_BASE_URL = `${STATISTICS_BASE_URL_ADMIN}/stocks`

//1)재고가 0인거의 통계
export const STOCK_STATISTICS_ZERO_URL = `${STOCK_STATISTICS_BASE_URL}/zero`
//2)지점별 in out loss
export const STOCK_STATISTICS_BRANCH_URL = `${STOCK_STATISTICS_BASE_URL}/branch`
//3)해당 년의 입고량 추이
export const STOCK_STATISTICS_TIME_URL = `${STOCK_STATISTICS_BASE_URL}/time`
//4)카테고리별 파이차트
export const STOCK_STATISTICS_CATEGORY_URL = `${STOCK_STATISTICS_BASE_URL}/category`

