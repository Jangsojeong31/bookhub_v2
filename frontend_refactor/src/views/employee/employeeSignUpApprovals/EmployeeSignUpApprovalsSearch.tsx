import { employeeSignUpApprovalSearchRequest } from "@/apis/employeeSignUpApprovals/employeeSignUpApprovals";
import { EmployeeSignUpApprovalsResponseDto } from "@/dtos/employee/response/employee-sign-up-approvals.response.dto";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/employee/employeeSelect.css";
import "@/styles/style.css";
import Pagination from "@/components/Pagination";
import usePagination from "@/hooks/usePagination";
import DataTable from "@/components/Table";

const isApprovedOptions = ["APPROVED", "DENIED"];
const ITEMS_PER_PAGE = 10;
const deniedReasonMap: Record<string, string> = {
  INVALID_EMPLOYEE_INFO: "사원 정보 불일치",
  ACCOUNT_ALREADY_EXISTS: "이미 계정이 발급된 사원",
  CONTRACT_EMPLOYEE_RESTRICTED: "계약직/기간제 사용 제한",
  PENDING_RESIGNATION: "퇴사 예정자",
};

function EmployeeSignUpApprovalsSearch() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({
    employeeName: "",
    authorizerName: "",
    isApproved: "",
    deniedReason: "",
    startUpdatedAt: "",
    endUpdatedAt: "",
  });

  const [employeeApprovalList, setEmployeeApprovalList] = useState<
    EmployeeSignUpApprovalsResponseDto[]
  >([]);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const onSearchClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const requestBody = {
      ...searchForm,
      deniedReason:
        searchForm.deniedReason === "" ? undefined : searchForm.deniedReason,
    };
    const response = await employeeSignUpApprovalSearchRequest(
      requestBody,
      token
    );

    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeApprovalList(data);
    } else {
      setEmployeeApprovalList([]);
    }
  };

  const onResetClick = () => {
    setSearchForm({
      employeeName: "",
      authorizerName: "",
      isApproved: "",
      deniedReason: "",
      startUpdatedAt: "",
      endUpdatedAt: "",
    });

    setEmployeeApprovalList([]);
  };

  const {
    currentPage,
    totalPages,
    pagedItems: paginatedEmployeeApprovalList,
    goToPage,
    goPrev,
    goNext,
  } = usePagination(employeeApprovalList, 10);

  return (
    <div>
      <div className="searchContainer">
        <h2>회원 가입 승인 로그 조회</h2>
        <div className="filter-bar">
          <input
            type="text"
            name="employeeName"
            value={searchForm.employeeName}
            placeholder="사원 명"
            onChange={onInputChange}
          />
          <input
            type="text"
            name="authorizerName"
            value={searchForm.authorizerName}
            placeholder="관리자 명"
            onChange={onInputChange}
          />
          <select
            name="isApproved"
            value={searchForm.isApproved}
            onChange={onInputChange}
          >
            <option value="">승인 상태를 선택하세요.</option>
            {isApprovedOptions.map((approved) => (
              <option key={approved} value={approved}>
                {approved == "APPROVED" ? "승인" : "거절"}
              </option>
            ))}
          </select>
          <select
            name="deniedReason"
            value={searchForm.deniedReason}
            onChange={onInputChange}
          >
            <option value="">거절 사유를 선택하세요.</option>
            <option value="INVALID_EMPLOYEE_INFO">사원 정보 불일치</option>
            <option value="ACCOUNT_ALREADY_EXISTS">
              이미 계정이 발급된 사원
            </option>
            <option value="PENDING_RESIGNATION">퇴사 예정자</option>
          </select>

          <input
            type="date"
            name="startUpdatedAt"
            value={searchForm.startUpdatedAt}
            placeholder="시작 연도"
            onChange={onInputChange}
          />
          <span>~</span>
          <input
            type="date"
            name="endUpdatedAt"
            value={searchForm.endUpdatedAt}
            placeholder="끝 연도"
            onChange={onInputChange}
          />

          <button onClick={onSearchClick}>검색</button>
          <button onClick={onResetClick}>초기화</button>
        </div>
      </div>

      <DataTable<EmployeeSignUpApprovalsResponseDto>
        columns={[
          {
            header: "",
            accessor: "number",
            cell: (_, index) => currentPage * ITEMS_PER_PAGE + index + 1,
          },
          { header: "사원 번호", accessor: "employeeNumber" },
          { header: "사원 명", accessor: "employeeName" },
          {
            header: "회원 가입 일자",
            accessor: "appliedAt",
            cell: (item) => new Date(item.appliedAt).toLocaleString(),
          },
          {
            header: "승인 상태",
            accessor: "isApproved",
            cell: (item) => (item.isApproved === "APPROVED" ? "승인" : "거절"),
          },
          {
            header: "거절 사유",
            accessor: "deniedReason",
            cell: (item) =>
              item.deniedReason
                ? deniedReasonMap[item.deniedReason] || item.deniedReason
                : "-",
          },
          { header: "관리자 사원 번호", accessor: "authorizerNumber" },
          { header: "관리자 명", accessor: "authorizerName" },
          {
            header: "승인/거절 일자",
            accessor: "updatedAt",
            cell: (item) => new Date(item.updatedAt).toLocaleString(),
          },
        ]}
        data={paginatedEmployeeApprovalList}
      />

      {paginatedEmployeeApprovalList.length > 0 && (
        <div className="footer">
          <Pagination
            currentPage={currentPage}
            totalPages={totalPages}
            onPageChange={goToPage}
            onPrev={goPrev}
            onNext={goNext}
          />
        </div>
      )}
    </div>
  );
}

export default EmployeeSignUpApprovalsSearch;
