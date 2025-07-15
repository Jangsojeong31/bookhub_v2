import { SidebarMenu } from './types';

export const commonMenu: SidebarMenu[] = [

  {
    title: '재고 관리',
    submenu: [
      { label: '내 지점 재고 관리', path: '/stocks/manager' },
      { label: '재고 로그 조회', path: '/stock-logs/branch' },
  
    ],
  },
  {
    title: '진열위치 관리',
    submenu: [
      { label: '진열위치 관리', path: '/branch/locations' },

    ],
  },
  {
    title: '발주',
    submenu: [
      { label: '발주 요청서 작성 및 조회', path: '/purchase-order' },
    ],
  },
  {
    title: '수령',
    submenu: [
      { label: '수령 확인', path: 'reception/pending' },
      { label: '수령 내역 조회', path: 'reception/confirmed' },
    ],
  },
  {
    title: '할인 정책',
    submenu: [
      { label: '정책 조회', path: '/policies' },
    ],
  },
  {
    title: '베스트 셀러',
    submenu: [
      { label: '총합 베스트셀러', path: '/best-seller' },
      { label: '기간별 베스트셀러', path: '/best-seller/period' },
      { label: '카테고리별 베스트셀러', path: '/best-seller/category' },
    ],
  },
  {
    title: '도서 검색',
    submenu: [
      { label: '통합 검색', path: '/books/search' },
    ],
  },
];
