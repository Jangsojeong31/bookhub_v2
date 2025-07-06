import { employeeChangeLogsSearchRequest } from "@/apis/employeeChangeLogs/employeeChangeLogs";
import { EmployeeChangeLogsResponseDto } from "@/dtos/employee/response/employee-change-logs.response.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";

const ITEMS_PER_PAGE = 10;
const changeTypeMap: Record<string, string> = {
  POSITION_CHANGE: "직급 변경",
  AUTHORITY_CHANGE: "권한 변경",
  BRANCH_CHANGE: "지점 변경",
};

function EmployeeChangeLogsSearch() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({
    employeeName: "",
    authorizerName: "",
    changeType: "",
    startUpdatedAt: "",
    endUpdatedAt: "",
  });
  const [employeeChangeLogs, setEmployeeChangeLogs] = useState<
    EmployeeChangeLogsResponseDto[]
  >([]);

  const [currentPage, setCurrentPage] = useState(0);
  const totalPages = Math.ceil(employeeChangeLogs.length / ITEMS_PER_PAGE);

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

    const response = await employeeChangeLogsSearchRequest(searchForm, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeChangeLogs(data);
    } else {
      setEmployeeChangeLogs([]);
    }
    setCurrentPage(0);
  };

  const onResetClcik = () => {
    setSearchForm({
      employeeName: "",
      authorizerName: "",
      changeType: "",
      startUpdatedAt: "",
      endUpdatedAt: "",
    });
    setEmployeeChangeLogs([]);
    setCurrentPage(0);
  };

  const paginatedEmployeeChangeLogList = employeeChangeLogs.slice(
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
        <h2>회원정보 로그 조회</h2>
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
            name="changeType"
            value={searchForm.changeType}
            onChange={onInputChange}
          >
            <option value="">변경 종류를 선택하세요</option>
            <option value="POSITION_CHANGE">직급 변경</option>
            <option value="AUTHORITY_CHANGE">권한 변경</option>
            <option value="BRANCH_CHANGE">지점 변경</option>
          </select>
          <input
            type="date"
            name="startUpdatedAt"
            value={searchForm.startUpdatedAt}
            placeholder="시작 일자"
            onChange={onInputChange}
          />
          <span>~</span>
          <input
            type="date"
            name="endUpdatedAt"
            value={searchForm.endUpdatedAt}
            placeholder="마지막 일자"
            onChange={onInputChange}
          />
          <div className="search-button">
            <button onClick={onSearchClick}>검색</button>
            <button onClick={onResetClcik}>초기화</button>
          </div>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <td></td>
            <th>사원 번호</th>
            <th>사원 명</th>
            <th>변경 종류</th>
            <th>이전 직급</th>
            <th>이전 권한</th>
            <th>이전 지점</th>
            <th>관리자 사원 번호</th>
            <th>관리자 명</th>
            <th>수정 일자</th>
          </tr>
        </thead>
        <tbody>
          {paginatedEmployeeChangeLogList.map((employeeChangeLog, index) => (
            <tr key={employeeChangeLog.logId}>
              <td>{currentPage * ITEMS_PER_PAGE + index + 1}</td>
              <td>{employeeChangeLog.employeeNumber}</td>
              <td>{employeeChangeLog.employeeName}</td>
              <td>
                {employeeChangeLog.changeType
                  ? changeTypeMap[employeeChangeLog.changeType] ||
                    employeeChangeLog.changeType
                  : "-"}
              </td>
              <td>
                {employeeChangeLog.prePositionName
                  ? employeeChangeLog.prePositionName
                  : "-"}
              </td>
              <td>
                {employeeChangeLog.preAuthorityName
                  ? employeeChangeLog.preAuthorityName
                  : "-"}
              </td>
              <td>
                {employeeChangeLog.preBranchName
                  ? employeeChangeLog.preBranchName
                  : "-"}
              </td>
              <td>{employeeChangeLog.authorizerNumber}</td>
              <td>{employeeChangeLog.authorizerName}</td>
              <td>{new Date(employeeChangeLog.updatedAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
      {employeeChangeLogs.length > 0 && (
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

export default EmployeeChangeLogsSearch;
