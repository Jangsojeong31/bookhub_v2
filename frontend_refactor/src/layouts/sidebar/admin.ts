// 📄 admin.ts
import { SidebarMenu } from './types';

export const adminMenu: SidebarMenu[] = [
  {
    title: '책 정보관리',
    submenu: [
      { label: '작가 관리', path: '/author/create' },       
      { label: '출판사 관리', path: '/publishers' },  
      { label: '카테고리 관리', path: '/categories' }, 
      { label: '책 등록', path: '/books/create' },
      { label: '책 수정 및 삭제', path: '/books/edit' },
      { label: '책 로그', path: '/booklogs' },
      { label: '', path:''  },
    ],
  },
  {
    title: '정책 관리',
    submenu: [
      { label: '정책 전체조회', path: '/policies' },
      { label: '정책 관리', path: '/policies/admin' },
    ],
  },
  {
    title: '지점 관리',
    submenu: [
      { label: '지점 조회', path: '/branches' },
      { label: '지점 관리', path: '/branches/manage' },
    ],
  },
  {
    title: '사원 관리',
    submenu: [
      { label: '로그인 승인', path: '/employees/approval' },
      { label: '사원정보 수정', path: '/employees/edit' },
      { label: '사원 정보 조회', path: '/employees' },
      { label: '퇴사자 로그 조회', path: '/employees/retired/logs' },
      { label: '회원정보 로그 조회', path: '/employees/logs' },
      { label: '회원가입승인 로그 조회', path: '/employees/approval/logs' },
      { label: '', path: '' },
      
    ],
  },
  {
    title: '발주 승인',
    submenu: [
      { label: '발주 승인', path: '/purchase-order/approve' },
      { label: '발주 승인 기록 조회', path: '/purchase-order-approval' },
    ],
  },
  {
    title: '수령 확인 관리',
    submenu: [
      { label: '수령 확인 내역', path: '/reception/logs' },
    ],
  },
  {
    title: '재고 로그 관리',
    submenu: [
      {label: '재고 로그 관리', path: '/stock-logs'}
    ]
  },
  {
    title: '판매 통계',
    submenu: [
      { label: '매출 통계', path: '/statistics/revenue' },
      { label: '재고 통계', path: '/statistics/stocks/branch' },
      { label: '판매량 통계', path: '/statistics/sales-quantity/period' },
    ],
  },
];
