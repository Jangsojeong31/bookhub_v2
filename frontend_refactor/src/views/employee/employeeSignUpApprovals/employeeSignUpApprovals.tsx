/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import { signUpResultRequest } from "@/apis/auth/auth";
import Modal from "@/components/Modal";
import {
  employeeSignUpApprovalRequest,
  employeeSignUpListeRequest,
} from "@/apis/employee/employee";
import { EmployeeSignUpListResponseDto } from "@/dtos/employee/response/employee-sign-up-list.response.dto copy";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/employee/employeeModal.css";
import "@/styles/employee/employeeSelect.css";
import DataTable from "@/components/Table";
import Pagination from "@/components/Pagination";
import usePagination from "@/hooks/usePagination";

const ITEMS_PER_PAGE = 10;

function EmployeeSignUpApprovals() {
  const [employeeList, setEmployeeList] = useState<
    EmployeeSignUpListResponseDto[]
  >([]);
  const [employee, setEmployee] = useState({ employeeId: 0, approvalId: 0 });
  const [message, setMessage] = useState("");

  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;

  const [modalStatus, setModalStatus] = useState(false);

  const [deniedReason, setDeniedReason] = useState("");

  const {
    currentPage,
    totalPages,
    pagedItems: paginatedEmployees,
    goToPage,
    goPrev,
    goNext,
  } = usePagination(employeeList, 10);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { value } = e.target;
    setDeniedReason(value);
  };

  const fetchEmployeSignUpList = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeSignUpListeRequest(token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeList(data);
      setMessage(message);
    } else {
      setEmployeeList([]);
      setMessage(message);
    }
  };

  useEffect(() => {
    fetchEmployeSignUpList();
  }, [token]);

  const onOpenModalClick = async (employee: EmployeeSignUpListResponseDto) => {
    setEmployee({
      employeeId: employee.employeeId,
      approvalId: employee.approvalId,
    });
    setModalStatus(true);
  };

  const onApprovedClick = async (employee: EmployeeSignUpListResponseDto) => {
    if (!token) {
      setMessage("인증 토큰이 없습니다.");
    }

    const response = await employeeSignUpApprovalRequest(
      employee.employeeId,
      { isApproved: "APPROVED", deniedReason: "" },
      token
    );

    const responseBody = await signUpResultRequest(employee.approvalId);

    const { code, message } = response;

    if (code === "SU" && responseBody.code === "SU") {
      alert(message + "\n이메일 전송 성공");
      fetchEmployeSignUpList();
    } else {
      alert(message + "\n이메일 전송 실패: " + responseBody.message);
      fetchEmployeSignUpList();
    }
  };

  const onSubmitClick = async () => {
    if (!token) {
      setMessage("인증 토큰이 없습니다.");
    }

    if (!deniedReason) {
      setMessage("거절 사유를 선택하세요");
      return;
    }

    const response = await employeeSignUpApprovalRequest(
      employee.employeeId,
      { isApproved: "DENIED", deniedReason: deniedReason },
      token
    );

    const responseBody = await signUpResultRequest(employee.approvalId);

    const { code, message } = response;

    if (code === "SU" && responseBody.code == "SU") {
      alert(message + "\n이메일 전송 성공");
      fetchEmployeSignUpList();
    } else {
      alert(message + "\n이메일 전송 실패: " + responseBody.message);
      fetchEmployeSignUpList();
    }

    setModalStatus(false);
  };

  const modalContent: React.ReactNode = (
    <>
      <div className="contain">
        <h1>거절 사유</h1>
        <select
          value={deniedReason}
          onChange={onInputChange}
          className="de-select"
        >
          <option value="">거절 사유 선택</option>
          <option value="INVALID_EMPLOYEE_INFO">사원 정보 불일치</option>
          <option value="ACCOUNT_ALREADY_EXISTS">
            이미 계정이 발급된 사원
          </option>
          <option value="PENDING_RESIGNATION">퇴사 예정자</option>
        </select>
        <button onClick={onSubmitClick} className="de-button">
          확인
        </button>
      </div>
    </>
  );

  return (
    <div>
      <h2>회원가입 승인</h2>
      <DataTable<EmployeeSignUpListResponseDto>
        columns={[
          {
            header: "",
            accessor: "number",
            cell: (_, index) => currentPage * ITEMS_PER_PAGE + index + 1,
          },
          { header: "사원 번호", accessor: "employeeNumber" },
          { header: "사원 명", accessor: "employeeName" },
          { header: "지점 명", accessor: "branchName" },
          { header: "이메일", accessor: "email" },
          { header: "전화 번호", accessor: "phoneNumber" },
          {
            header: "회원 가입 일자",
            accessor: "appliedAt",
            cell: (item) => new Date(item.appliedAt || "").toLocaleString(),
          },
          {
            header: "승인 상태",
            accessor: "isApproved",
            cell: (item) =>
              item.isApproved === "PENDING" ? "대기 중" : "오류",
          },
        ]}
        data={paginatedEmployees}
        actions={[
          {
            label: "승인",
            onClick: onApprovedClick,
            buttonCss: style.modifyButton,
          },
          {
            label: "거절",
            onClick: onOpenModalClick,
            buttonCss: style.deleteButton,
          },
        ]}
      />

      {paginatedEmployees.length > 0 && (
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

      {modalStatus && (
        <Modal
          isOpen={modalStatus}
          onClose={() => setModalStatus(false)}
          children={modalContent}
        />
      )}
    </div>
  );
}

export default EmployeeSignUpApprovals;
