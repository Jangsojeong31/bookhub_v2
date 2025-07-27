/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import { PolicyType } from "@/apis/enums/PolicyType";
import {
  getPolicies,
  deletePolicy,
  getPolicyDetail,
} from "@/apis/policy/policy";
import {
  PolicyListResponseDto,
  PolicyDetailResponseDto,
} from "@/dtos/policy/policy.response.dto";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import CreatePolicy from "./CreatePolicy";
import UpdatePolicy from "./UpdatePolicy";
import "./policyC.css";
import DataTable from "@/components/Table";

const PAGE_SIZE = 10;

const PolicyPage: React.FC = () => {
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

  const [isCreateOpen, setIsCreateOpen] = useState(false);
  const [isUpdateOpen, setIsUpdateOpen] = useState(false);

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

  const handleDelete = async (id: number) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    if (!accessToken) return;
    try {
      const res = await deletePolicy(id, accessToken);
      if (res.code === "SU") {
        const isLast = policies.length === 1 && currentPage > 0;
        fetchPage(isLast ? currentPage - 1 : currentPage);
      } else {
        alert(res.message || "삭제 실패");
      }
    } catch (err) {
      console.error("삭제 예외:", err);
      alert("삭제 중 오류 발생");
    }
  };

  const openUpdateModal = async (id: number) => {
    if (!accessToken) return;
    try {
      const res = await getPolicyDetail(id, accessToken);
      if (res.code === "SU" && res.data) {
        setSelectedDetail(res.data);
        setSelectedPolicyId(id);
        setIsUpdateOpen(true);
      } else {
        alert(res.message || "상세 조회 실패");
      }
    } catch (err) {
      console.error("상세 조회 예외:", err);
      alert("상세 조회 중 오류 발생");
    }
  };

  const handleUpdateClose = () => {
    setSelectedDetail(null);
    setIsUpdateOpen(false);
  };

  const handleUpdated = () => {
    handleUpdateClose();
    fetchPage(currentPage);
  };

  const goToPage = (page: number) => {
    if (page < 0 || page >= totalPages) return;
    fetchPage(page);
  };

  return (
    <div className="policy-page-container">
      <div className="filter-bar">
        <button
          className="left-item"
          onClick={() => setIsCreateOpen(true)}
          css={style.createButton}
        >
          정책 등록
        </button>
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
          placeholder="제목 검색"
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
        <button onClick={() => goToPage(0)} css={style.searchButton}>
          검색
        </button>
      </div>

      <div className="table-policy">
        <DataTable<PolicyListResponseDto>
          columns={[
            { header: "정책 ID", accessor: "policyId" },
            { header: "제목", accessor: "policyTitle" },
            { header: "타입", accessor: "policyType" },
            { header: "시작일", accessor: "startDate" },
            { header: "종료일", accessor: "endDate" },
          ]}
          data={policies}
          actions={[
            {
              label: "수정",
              onClick: (p) => openUpdateModal(p.policyId),
              buttonCss: style.modifyButton,
            },
            {
              label: "삭제",
              onClick: (p) => handleDelete(p.policyId),
              buttonCss: style.deleteButton,
            },
          ]}
        />
      </div>

      <div className="pagination">
        <button
          className="btn-primary"
          disabled={currentPage === 0}
          onClick={() => goToPage(currentPage - 1)}
        >
          이전
        </button>
        <span>
          {currentPage + 1} / {totalPages}
        </span>
        <button
          className="btn-primary"
          disabled={currentPage + 1 >= totalPages}
          onClick={() => goToPage(currentPage + 1)}
        >
          다음
        </button>
      </div>

      {isCreateOpen && (
        <CreatePolicy
          isOpen={isCreateOpen}
          onClose={() => setIsCreateOpen(false)}
          onCreated={() => fetchPage(currentPage)}
        />
      )}

      {isUpdateOpen && selectedDetail && selectedPolicyId != null && (
        <UpdatePolicy
          isOpen={isUpdateOpen}
          onClose={handleUpdateClose}
          onUpdated={handleUpdated}
          policyDetail={selectedDetail}
          policyId={selectedPolicyId}
        />
      )}
    </div>
  );
};

export default PolicyPage;
