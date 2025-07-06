/** @jsxImportSource @emotion/react */
import { css } from '@emotion/react';

export const bodyStyle = css`
  background-color: white; /* 전체 배경색을 약간 밝게 */
  font-family: 'Arial', sans-serif;
  margin: 0;
  padding: 0;
`;

/* 상단 바: 등록 버튼 및 검색창을 수평 배치 */
export const topBar = css`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 24px;
  background-color: white;
  border-bottom: 1px solid #ddd;
`;

/* 상단바 - 등록 버튼 */
export const createButton = css `
  background-color: #265185;
  color: white;
  font-weight: bold;
  padding: 8px 16px;
  cursor: pointer;
  border: none;
  border-radius: 4px;
`;

/* 검색창 */
export const searchInput = css `
  width: 300px;
  max-width: 60%;
  height: 36px;
  border: 1px solid #ccc;
  outline: none;
  border-radius: 18px; /* 둥글게 */
  padding: 0 16px;
  font-size: 14px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.2s ease;
  &:focus {
    box-shadow: 0 1px 5px rgba(0, 0, 0, 0.2);
  }
`;

/* 테이블 컨테이너: 스크롤 가능하도록 가로 여유 주기 */
export const tableContainer = css`
  width: 100%;
  overflow-x: auto;
`;

/* 테이블 기본 */
export const table = css`
  width: 100%;
  margin: 16px auto;
  border-collapse: collapse;
  background-color: white;
`;

/* 헤더 행 스타일 */
export const thead = css `
  background-color: #2b5480;
  color: white;
`;

/* th, td */
export const th = css`
  border: 1px solid #ccc;
  padding: 12px;
  text-align: center;
  font-size: 14px;
`;

export const td = css`
  border: 1px solid #ccc;
  padding: 12px;
  text-align: center;
  font-size: 14px;
`;

/* 높이 고정 행 (optional) */
export const tableheight = css `{
  height: 48px;
`;

/* 수정/삭제 버튼 */
export const deleteButton = css `
  background-color: #e74c3c;
  color: white;
  font-size: 12px;
  border: none;
  border-radius: 12px;
  padding: 4px 10px;
  margin: 0 4px;
  cursor: pointer;
`;

export const modifyButton = css `
  background-color: #2b5480;
  color: white;
  font-size: 12px;
  border: none;
  border-radius: 12px;
  padding: 4px 10px;
  margin: 0 4px;
  cursor: pointer;

  &:disabled {
    color: #aaa;
    cursor: not-allowed;
  }
`;

/* footer(페이지네이션) */
export const footer = css`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 16px 0;
  gap: 8px;
`;

/* 페이지 번호 버튼 */
export const pageBtn = css`
  border: none;
  background: none;
  font-size: 14px;
  cursor: pointer;

  &.current {
    font-weight: bold;
    text-decoration: underline;
  }
`;

/* 페이지 정보 텍스트 */
export const pageText = css`
  font-size: 14px;
  margin-left: 12px;
`;

/* 전체 조회 버튼 */
export const searchAll = css`
  background-color: #e74c3c;
  color: white;
  font-size: 12px;
  border: none;
  border-radius: 5px;
  padding: 6px 12px;
  margin-left: 24px;
  cursor: pointer;
`;

/* 모달 헤더 (제목) */
export const publisherModalHeader = css`
  margin: 0 0 12px;
  font-size: 18px;
  color: #265185;
`;

/* 입력 필드 */
export const publisherInput = css`
  width: 100%;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-bottom: 8px;
`;

/* 에러 메시지 */
export const publisherErrorMessage = css`
  color: red;
  font-size: 12px;
  margin: 4px 0;
`;

/* 버튼 그룹 */
export const publisherButtonGroup = css`
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 12px;
`;
