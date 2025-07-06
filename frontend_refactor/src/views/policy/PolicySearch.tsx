import { PolicyType } from "@/apis/enums/PolicyType";
import { getPolicies, getPolicyDetail } from "@/apis/policy/policy";
import { PolicyDetailResponseDto, PolicyListResponseDto } from "@/dtos/policy/policy.response.dto";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import PolicyDetail from "./PolicyDetail";
import './policyC.css';

const PAGE_SIZE = 10;

const PolicySearch: React.FC = () => {
  const [cookies] = useCookies(['accessToken']);
  const accessToken = cookies.accessToken;

  const [type, setType] = useState<PolicyType | ''>('');
  const [startDate, setStartDate] = useState<string>('');
  const [endDate, setEndDate] = useState<string>('');
  const [keyword, setKeyword] = useState<string>('');
  const [discountPercent, setDiscountPercent] = useState<number | ''>('');

  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [policies, setPolicies] = useState<PolicyListResponseDto[]>([]);
  const [selectedPolicyId, setSelectedPolicyId] = useState<number | null>(null);
  const [selectedDetail, setSelectedDetail] = useState<PolicyDetailResponseDto | null>(null);
  const [isDetailOpen, setIsDetailOpen] = useState(false);

  const fetchPage = async (page: number) => {
    if (!accessToken) return;
    try {
      const res = await getPolicies(
        accessToken,
        page,
        PAGE_SIZE,
        keyword.trim() || undefined,
        type || undefined,
        startDate || undefined,
        endDate || undefined
      );

      if (res.code === 'SU' && res.data) {
        const data = res.data;
        if ('content' in data) {
          setPolicies(data.content);
          setTotalPages(data.totalPages);
          setCurrentPage(data.currentPage);
        } else {
          setPolicies(data as PolicyListResponseDto[]);
          setTotalPages(1);
          setCurrentPage(0);
        }
      } else {
        console.error('목록 조회 실패:', res.message);
      }
    } catch (err) {
      console.error('목록 조회 예외:', err);
    }
  };

  useEffect(() => {
    fetchPage(0);
  }, [accessToken, keyword, type, startDate, endDate]);

  const openDetailModal = async (id: number) => {
    if (!accessToken) return;
    try {
      const res = await getPolicyDetail(id, accessToken);
      if (res.code === 'SU' && res.data) {
        setSelectedDetail(res.data);
        setSelectedPolicyId(id);
        setIsDetailOpen(true);
      } else {
        alert(res.message || '상세 조회 실패');
      }
    } catch (err) {
      console.error('상세 조회 예외:', err);
      alert('상세 조회 중 오류 발생');
    }
  };

  const handleDetailClose = () => {
    setSelectedDetail(null);
    setIsDetailOpen(false);
  };

  const goToPage = (page: number) => {
    if (page < 0 || page >= totalPages) return;
    fetchPage(page);
  };

  return (
    <div className="policy-page-container">
      <div className="filter-bar">
        <select value={type} onChange={(e) => setType(e.target.value as PolicyType)}>
          <option value="">전체</option>
          <option value={PolicyType.BOOK_DISCOUNT}>도서 할인</option>
          <option value={PolicyType.TOTAL_PRICE_DISCOUNT}>총 금액 할인</option>
          <option value={PolicyType.CATEGORY_DISCOUNT}>카테고리 할인</option>
        </select>
        <input type="date" value={startDate} onChange={(e) => setStartDate(e.target.value)} />
        <input type="date" value={endDate} onChange={(e) => setEndDate(e.target.value)} />
        <input
          type="text"
          placeholder="제목 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && goToPage(0)}
          className="input-search"
        />
        <input
          type="number"
          placeholder="할인율(%)"
          value={discountPercent}
          onChange={(e) => setDiscountPercent(e.target.value === '' ? '' : Number(e.target.value))}
          className="input-search"
        />
        <button onClick={() => goToPage(0)} className="btn-primary">
          검색
        </button>
      </div>

      <table className="table-policy">
        <thead>
          <tr>
            <th>정책 ID</th>
            <th>제목</th>
            <th>타입</th>
            <th>시작일</th>
            <th>종료일</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>
          {policies.map((p) => (
            <tr key={p.policyId}>
              <td>{p.policyId}</td>
              <td>{p.policyTitle}</td>
              <td>{p.policyType}</td>
              <td>{p.startDate}</td>
              <td>{p.endDate}</td>
              <td>
                <button onClick={() => openDetailModal(p.policyId)} className="modifyBtn">
                  보기
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="pagination">
        <button className="modifyBtn" disabled={currentPage === 0} onClick={() => goToPage(currentPage - 1)}>
          이전
        </button>
        <span>{currentPage + 1} / {totalPages}</span>
        <button className="modifyBtn" disabled={currentPage + 1 >= totalPages} onClick={() => goToPage(currentPage + 1)}>
          다음
        </button>
      </div>

      {isDetailOpen && selectedDetail && selectedPolicyId != null && (
        <PolicyDetail
          isOpen={isDetailOpen}
          onClose={handleDetailClose}
          policyDetail={selectedDetail}
          policyId={selectedPolicyId}
        />
      )}
    </div>
  );
};

export default PolicySearch;

