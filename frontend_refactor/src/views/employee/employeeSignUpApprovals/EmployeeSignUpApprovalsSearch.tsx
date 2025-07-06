import { employeeSignUpApprovalSearchRequest } from "@/apis/employeeSignUpApprovals/employeeSignUpApprovals";
import { EmployeeSignUpApprovalsResponseDto } from "@/dtos/employee/response/employee-sign-up-approvals.response.dto";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/employee/employeeSelect.css";

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
  const [currentPage, setCurrentPage] = useState(0);
  const totalPages = Math.ceil(employeeApprovalList.length / ITEMS_PER_PAGE);

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

    setCurrentPage(0);
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
    setCurrentPage(0);
  };

  const paginatedEmployeeApprovalList = employeeApprovalList.slice(
    currentPage * ITEMS_PER_PAGE,
    (currentPage + 1) * ITEMS_PER_PAGE
  );

  const goToPage = (page: number) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  const goPrev = () => {
    if (currentPage > 0) setCurrentPage(currentPage - 1);
  };

  const goNext = () => {
    if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
  };

  return (
    <div>
      <div className="searchContainer">
        <h2>회원 가입 승인 로그 조회</h2>
        <div className="search-row">
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

          <div className="search-button">
            <button onClick={onSearchClick}>검색</button>
            <button onClick={onResetClick}>초기화</button>
          </div>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th></th>
            <th>사원 번호</th>
            <th>사원 명</th>
            <th>회원 가입 일자</th>
            <th>승인 상태</th>
            <th>거절 사유</th>
            <th>관리자 사원 번호</th>
            <th>관리자 명</th>
            <th>승인/거절 일자</th>
          </tr>
        </thead>
        <tbody>
          {paginatedEmployeeApprovalList.map(
            (paginatedEmployeeApproval, index) => (
              <tr key={paginatedEmployeeApproval.approvalId}>
                <td>{currentPage * ITEMS_PER_PAGE + index + 1}</td>
                <td>{paginatedEmployeeApproval.employeeNumber}</td>
                <td>{paginatedEmployeeApproval.employeeName}</td>
                <td>
                  {new Date(
                    paginatedEmployeeApproval.appliedAt
                  ).toLocaleString()}
                </td>
                <td>
                  {paginatedEmployeeApproval.isApproved === "APPROVED"
                    ? "승인"
                    : "거절"}
                </td>
                <td>
                  {paginatedEmployeeApproval.deniedReason
                    ? deniedReasonMap[paginatedEmployeeApproval.deniedReason] ||
                      paginatedEmployeeApproval.deniedReason
                    : "-"}
                </td>
                <td>{paginatedEmployeeApproval.authorizerNumber}</td>
                <td>{paginatedEmployeeApproval.authorizerName}</td>
                <td>
                  {" "}
                  {new Date(
                    paginatedEmployeeApproval.updatedAt
                  ).toLocaleString()}
                </td>
              </tr>
            )
          )}
        </tbody>
      </table>
      {employeeApprovalList.length > 0 && (
        <div className="footer">
          <button
            className="pageBtn"
            onClick={goPrev}
            disabled={currentPage === 0}
          >
            {"<"}
          </button>
          {Array.from({ length: totalPages }, (_, i) => i).map((i) => (
            <button
              key={i}
              className={`pageBtn${i === currentPage ? " current" : ""}`}
              onClick={() => goToPage(i)}
            >
              {i + 1}
            </button>
          ))}
          <button
            className="pageBtn"
            onClick={goNext}
            disabled={currentPage >= totalPages - 1}
          >
            {">"}
          </button>
          <span className="pageText">
            {totalPages > 0 ? `${currentPage + 1} / ${totalPages}` : "0 / 0"}
          </span>
        </div>
      )}
    </div>
  );
}

export default EmployeeSignUpApprovalsSearch;
