/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import { PolicyType } from "@/apis/enums/PolicyType";
import { getPolicies, getPolicyDetail } from "@/apis/policy/policy";
import {
  PolicyDetailResponseDto,
  PolicyListResponseDto,
} from "@/dtos/policy/policy.response.dto";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import PolicyDetail from "./PolicyDetail";
import "./policyC.css";
import DataTable from "@/components/Table";

const PAGE_SIZE = 10;

const PolicySearch: React.FC = () => {
  const [cookies] = useCookies(["accessToken"]);
  const accessToken = cookies.accessToken;

  const [type, setType] = useState<PolicyType | "">("");
  const [startDate, setStartDate] = useState<string>("");
  const [endDate, setEndDate] = useState<string>("");
  const [keyword, setKeyword] = useState<string>("");
  const [discountPercent, setDiscountPercent] = useState<number | "">("");

  const [currentPage, setCurrentPage] = useState<number>(0);
  const [totalPages, setTotalPages] = useState<number>(0);
  const [policies, setPolicies] = useState<PolicyListResponseDto[]>([]);
  const [selectedPolicyId, setSelectedPolicyId] = useState<number | null>(null);
  const [selectedDetail, setSelectedDetail] =
    useState<PolicyDetailResponseDto | null>(null);
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

      if (res.code === "SU" && res.data) {
        const data = res.data;
        if ("content" in data) {
          setPolicies(data.content);
          setTotalPages(data.totalPages);
          setCurrentPage(data.currentPage);
        } else {
          setPolicies(data as PolicyListResponseDto[]);
          setTotalPages(1);
          setCurrentPage(0);
        }
      } else {
        console.error("목록 조회 실패:", res.message);
      }
    } catch (err) {
      console.error("목록 조회 예외:", err);
    }
  };

  useEffect(() => {
    fetchPage(0);
  }, [accessToken, keyword, type, startDate, endDate]);

  const openDetailModal = async (id: number) => {
    if (!accessToken) return;
    try {
      const res = await getPolicyDetail(id, accessToken);
      if (res.code === "SU" && res.data) {
        setSelectedDetail(res.data);
        setSelectedPolicyId(id);
        setIsDetailOpen(true);
      } else {
        alert(res.message || "상세 조회 실패");
      }
    } catch (err) {
      console.error("상세 조회 예외:", err);
      alert("상세 조회 중 오류 발생");
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
      <h2>할인 정책 조회</h2>
      <div className="filter-bar">
        <select
          value={type}
          onChange={(e) => setType(e.target.value as PolicyType)}
        >
          <option value="">할인 유형</option>
          <option value={PolicyType.BOOK_DISCOUNT}>도서 할인</option>
          <option value={PolicyType.TOTAL_PRICE_DISCOUNT}>총 금액 할인</option>
          <option value={PolicyType.CATEGORY_DISCOUNT}>카테고리 할인</option>
        </select>
        <p>시작일</p>
        <input
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
        />
        <p>종료일</p>
        <input
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
        />
        <input
          type="text"
          placeholder="할인 제목"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          onKeyDown={(e) => e.key === "Enter" && goToPage(0)}
        />
        <input
          type="number"
          placeholder="할인율(%)"
          value={discountPercent}
          onChange={(e) =>
            setDiscountPercent(
              e.target.value === "" ? "" : Number(e.target.value)
            )
          }
        />
        <button onClick={() => goToPage(0)}>검색</button>
      </div>

      <table className="table-policy">
        <DataTable<PolicyListResponseDto>
          columns={[
            { header: "정책 ID", accessor: "policyId" },
            { header: "제목", accessor: "policyTitle" },
            {
              header: "타입",
              accessor: "policyType",
              cell: (item) =>
                item.policyType == PolicyType.BOOK_DISCOUNT
                  ? "도서 할인"
                  : item.policyType == PolicyType.CATEGORY_DISCOUNT
                  ? "카테고리 할인"
                  : "총액 할인",
            },
            { header: "시작일", accessor: "startDate" },
            { header: "종료일", accessor: "endDate" },
          ]}
          data={policies}
          actions={[
            {
              label: "보기",
              onClick: (p) => openDetailModal(p.policyId),
              buttonCss: style.modifyButton,
            },
          ]}
        />
      </table>

      <div className="pagination">
        <button
          className="modifyBtn"
          disabled={currentPage === 0}
          onClick={() => goToPage(currentPage - 1)}
        >
          이전
        </button>
        <span>
          {currentPage + 1} / {totalPages}
        </span>
        <button
          className="modifyBtn"
          disabled={currentPage + 1 >= totalPages}
          onClick={() => goToPage(currentPage + 1)}
        >
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
