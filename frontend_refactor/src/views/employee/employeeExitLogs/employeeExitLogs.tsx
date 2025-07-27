import { employeeExitLogsRequest } from "@/apis/employeeExitLogs/employeeExitLogs";
import Pagination from "@/components/Pagination";
import { EmployeeExitLogsResponseDto } from "@/dtos/employee/response/employee-exit-logs.response.dto";
import usePagination from "@/hooks/usePagination";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/style.css";

const ITEMS_PAGE = 10;
const exitReasonMap: Record<string, string> = {
  RETIREMENT: "정년 퇴직",
  VOLUNTEER: "자진 퇴사",
  FORCED: "권고 사직",
  TERMINATED: "해고",
};

function EmployeeExitLogs() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;

  const [searchForm, setSearchFrom] = useState({
    employeeName: "",
    authorizerName: "",
    exitReason: "",
    startUpdatedAt: "",
    endUpdatedAt: "",
  });

  const [employeeExitLogs, setEmployeeExitLogs] = useState<
    EmployeeExitLogsResponseDto[]
  >([]);
  
  const {
      currentPage,
      totalPages,
      pagedItems: paginatedEmployeeExitLogs,
      goToPage,
      goPrev,
      goNext,
    } = usePagination(employeeExitLogs, 10);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setSearchFrom({ ...searchForm, [name]: value });
  };

  const onSearchClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeExitLogsRequest(searchForm, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeExitLogs(data);
    } else {
      setEmployeeExitLogs([]);
    }

  };

  const onResetClick = () => {
    setSearchFrom({
      employeeName: "",
      authorizerName: "",
      exitReason: "",
      startUpdatedAt: "",
      endUpdatedAt: "",
    });

    setEmployeeExitLogs([]);
  };

    return (
    <div>
      <div className="searchContainer">
        <h2>퇴사자 로그 조회</h2>
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
            name="exitReason"
            value={searchForm.exitReason}
            onChange={onInputChange}
          >
            <option value="">퇴사 사유를 선택하세요.</option>
            <option value="RETIREMENT">정년 퇴직</option>
            <option value="VOLUNTEER">자진 퇴사</option>
            <option value="FORCED">권고 사직</option>
            <option value="TERMINATED">해고</option>
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
            placeholder="종료 일자"
            onChange={onInputChange}
          />

            <button onClick={onSearchClick}>검색</button>
            <button onClick={onResetClick}>초기화</button>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <td></td>
            <td>사원 번호</td>
            <td>사원 명</td>
            <td>지점 명</td>
            <td>직급 명</td>
            <td>상태</td>
            <td>퇴사 사유</td>
            <td>권한자 사원 번호</td>
            <td>권한자 명</td>
            <td>퇴사 일자</td>
          </tr>
        </thead>
        <tbody>
          {paginatedEmployeeExitLogs.map((employeeExitLog, index) => (
            <tr key={employeeExitLog.exitId}>
              <td>{currentPage * ITEMS_PAGE + index + 1}</td>
              <td>{employeeExitLog.employeeNumber}</td>
              <td>{employeeExitLog.employeeName}</td>
              <td>{employeeExitLog.branchName}</td>
              <td>{employeeExitLog.positionName}</td>
              <td>{employeeExitLog.status === "EXITED" ? "퇴사" : "재직"}</td>
              <td>
                {employeeExitLog.exitReason
                  ? exitReasonMap[employeeExitLog.exitReason] ||
                    employeeExitLog.exitReason
                  : "-"}
              </td>
              <td>{employeeExitLog.authorizerNumber}</td>
              <td>{employeeExitLog.authorizerName}</td>
              <td>{new Date(employeeExitLog.updatedAt).toLocaleString()}</td>
            </tr>
          ))}
        </tbody>
      </table>
      
      {employeeExitLogs.length > 0 && (
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

export default EmployeeExitLogs;
