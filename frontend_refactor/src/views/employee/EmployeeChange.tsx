import {
  GET_ALL_AUTHORITY_URL,
  GET_ALL_POSITION_URL,
  GET_BRANCH_URL,
} from "@/apis";
import Modal from "@/apis/constants/Modal";
import {
  employeeChangeRequestDto,
  employeeDetailRequest,
  employeeExitUpdateRequest,
  employeeRequest,
} from "@/apis/employee/employee";
import { Authority } from "@/dtos/authority/authority";
import { EmployeeDetailResponseDto } from "@/dtos/employee/response/employee-detail.response.dto";
import { EmployeeListResponseDto } from "@/dtos/employee/response/employee-list.response.dto";
import { Position } from "@/dtos/position/position";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/employee/employeeUpdateModal.css";
import "@/styles/employee/employeemodal.css";
import "@/styles/employee/employeeSelect.css";
import { Branch } from "@/dtos/branch/branch";

const statusOptions = ["EMPLOYED", "EXITED"];
const ITEMS_PER_PAGE = 10;

function EmployeeChange() {
  const [searchForm, setSearchForm] = useState({
    name: "",
    branchName: "",
    positionName: "",
    authorityName: "",
    status: "",
  });

  const [currentPage, setCurrentPage] = useState(0);
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;

  const [employeeList, setEmployeeList] = useState<EmployeeListResponseDto[]>(
    []
  );
  const [message, setMessage] = useState("");
  const [branches, setBranches] = useState<Branch[]>([]);
  const [positions, setPositions] = useState<Position[]>([]);
  const [authorities, setAuthorities] = useState<Authority[]>([]);
  const [modalStatus, setModalStatus] = useState(false);
  const [modalExitStatus, setModalExitStatus] = useState(false);
  const [employee, setEmployee] = useState<EmployeeDetailResponseDto>({
    employeeId: 0,
    employeeNumber: 0,
    employeeName: "",
    branchId: 0,
    branchName: "",
    positionId: 0,
    positionName: "",
    authorityId: 0,
    authorityName: "",
    email: "",
    phoneNumber: "",
    birthDate: new Date(),
    status: "EMPLOYED",
    createdAt: new Date(),
  });

  const [form, setForm] = useState({
    branchId: 0,
    positionId: 0,
    authorityId: 0,
  });

  const [exit, setExit] = useState({
    status: "EMPLOYED",
    exitReason: "",
  });

  const totalPages = Math.ceil(employeeList.length / ITEMS_PER_PAGE);

  useEffect(() => {
    fetch(`${GET_BRANCH_URL}?branchLocation`, {
      method: "GET",
    })
      .then((res) => res.json())
      .then((data) => {
        setBranches(data.data);
      })
      .catch((e) => console.error(e));

    fetch(`${GET_ALL_POSITION_URL}`, {
      method: "GET",
    })
      .then((res) => res.json())
      .then((data) => {
        setPositions(data.data);
      })
      .catch((e) => console.error(e));

    fetch(`${GET_ALL_AUTHORITY_URL}`, {
      method: "GET",
    })
      .then((res) => res.json())
      .then((data) => {
        setAuthorities(data.data);
      })
      .catch((e) => console.error(e));
  }, []);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const onExitReasonSelectChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setExit({ ...exit, [name]: value });
  };

  const onEmployeeChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: Number(value) });
  };

  const onSearchClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeRequest(searchForm, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeList(data);
    } else {
      setEmployeeList([]);
      setMessage(message);
      return;
    }

    setCurrentPage(0);
  };

  const paginatedEmployees = employeeList.slice(
    currentPage * ITEMS_PER_PAGE,
    (currentPage + 1) * ITEMS_PER_PAGE
  );

  const onOpenModalUpdateClick = async (employee: EmployeeListResponseDto) => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeDetailRequest(employee.employeeId, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployee(data);
      setForm({
        branchId: data.branchId,
        positionId: data.positionId,
        authorityId: data.authorityId,
      });
    } else {
      setMessage(message);
      return;
    }

    setModalStatus(true);
  };

  const onOpenModalExitUpdateClick = async (
    employee: EmployeeListResponseDto
  ) => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    if (employee.status === "EXITED") {
      alert("이미 퇴사 처리 되었습니다.");
      return;
    }

    const response = await employeeDetailRequest(employee.employeeId, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployee(data);
      setExit({ status: data.status, exitReason: "" });
    } else {
      setMessage(message);
      return;
    }

    setModalExitStatus(true);
  };

  const onUpdateClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeChangeRequestDto(
      employee.employeeId,
      form,
      token
    );

    const { code, message } = response;

    if (code === "SU") {
      alert(message);
    } else {
      alert(message);
      return;
    }

    setModalStatus(false);

    onSearchClick();
  };

  const onExitUpdateClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    if (exit.exitReason == "") {
      alert("항목을 선택해주세요");
      return;
    }

    const response = await employeeExitUpdateRequest(
      employee.employeeId,
      {
        status: "EXITED",
        exitReason: exit.exitReason,
      },
      token
    );

    const { code, message } = response;

    if (code === "SU") {
      alert(message);
    } else {
      alert("이미 퇴사 처리 되었습니다.");
    }

    setModalExitStatus(false);

    onSearchClick();
  };

  const onResetButtonClick = () => {
    setSearchForm({
      name: "",
      branchName: "",
      positionName: "",
      authorityName: "",
      status: "",
    });
    setEmployeeList([]);
    setCurrentPage(0);
  };

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

  const modalContent: React.ReactNode = (
    <>
      <div className="employee-details">
        <h1>사원 세부 사항</h1>
        <div className="vertical-row">
          <div className="column">
            <div className="field">
              <label>사원 번호</label>
              <span>{employee.employeeNumber}</span>
            </div>
            <div className="field">
              <label>사원 이름</label>
              <span>{employee.employeeName}</span>
            </div>
            <div className="field">
              <label>지점 명</label>
              <select
                name="branchId"
                value={form.branchId}
                onChange={onEmployeeChange}
              >
                <option value="">지점을 선택하세요</option>
                {branches.map((branch) => (
                  <option key={branch.branchId} value={branch.branchId}>
                    {branch.branchName}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label>직급 명</label>
              <select
                name="positionId"
                value={form.positionId}
                onChange={onEmployeeChange}
              >
                <option value="">직급 선택</option>
                {positions.map((position) => (
                  <option key={position.positionId} value={position.positionId}>
                    {position.positionName}
                  </option>
                ))}
              </select>
            </div>
            <div className="field">
              <label>권한 명</label>
              <select
                name="authorityId"
                value={form.authorityId}
                onChange={onEmployeeChange}
              >
                <option value="">권한 선택</option>
                {authorities.map((auth) => (
                  <option key={auth.authorityId} value={auth.authorityId}>
                    {auth.authorityName}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className="column">
            <div className="field">
              <label>이메일</label>
              <span>{employee.email}</span>
            </div>
            <div className="field">
              <label>전화 번호</label>
              <span>{employee.phoneNumber}</span>
            </div>
            <div className="field">
              <label>생년월일</label>
              <span>
                {new Date(employee.birthDate || "").toLocaleDateString()}
              </span>
            </div>
            <div className="field">
              <label>재직 상태</label>
              <span>{employee.status === "EXITED" ? "퇴사" : "재직"}</span>
            </div>
            <div className="field">
              <label>입사 일자</label>
              <span>{new Date(employee.createdAt || "").toLocaleString()}</span>
            </div>
          </div>
        </div>
        <div className="actions">
          <button onClick={onUpdateClick}>수정</button>
        </div>
      </div>
    </>
  );

  const modalExitContent: React.ReactNode = (
    <>
      <div className="contain">
        <h1>퇴직 처리</h1>
        <select
          name="exitReason"
          value={exit.exitReason}
          onChange={onExitReasonSelectChange}
          className="de-select"
        >
          <option value="">퇴직 사유를 선택하세요</option>
          <option value="RETIREMENT">정년 퇴직</option>
          <option value="VOLUNTEER">자진 퇴사</option>
          <option value="FORCED">권고 사직</option>
          <option value="TERMINATED">해고</option>
        </select>
        <button onClick={onExitUpdateClick} className="de-button">
          퇴사
        </button>
      </div>
    </>
  );

  return (
    <>
      <div>
        <div className="searchContainer">
          <h2>사원 정보 수정</h2>
          <div className="search-row">
            <input
              type="text"
              name="name"
              placeholder="이름"
              value={searchForm.name}
              onChange={onInputChange}
            />
            <select
              name="branchName"
              value={searchForm.branchName}
              onChange={onInputChange}
            >
              <option value="">지점 선택</option>
              {branches.map((branch) => (
                <option key={branch.branchName} value={branch.branchName}>
                  {branch.branchName}
                </option>
              ))}
            </select>
            <select
              name="positionName"
              value={searchForm.positionName}
              onChange={onInputChange}
            >
              <option value="">직급 선택</option>
              {positions.map((p) => (
                <option key={p.positionName} value={p.positionName}>
                  {p.positionName}
                </option>
              ))}
            </select>

            <select
              name="authorityName"
              value={searchForm.authorityName}
              onChange={onInputChange}
            >
              <option value="">권한 선택</option>
              {authorities.map((a) => (
                <option key={a.authorityName} value={a.authorityName}>
                  {a.authorityName}
                </option>
              ))}
            </select>
            <select
              name="status"
              value={searchForm.status}
              onChange={onInputChange}
            >
              <option value="">상태 선택</option>
              {statusOptions.map((s) => (
                <option key={s} value={s}>
                  {s === "EXITED" ? "퇴사" : "재직"}
                </option>
              ))}
            </select>
            <div className="search-button">
              <button onClick={onSearchClick}>검색</button>
              <button onClick={onResetButtonClick}>초기화</button>
            </div>
          </div>
        </div>
        {message && <p style={{ color: "red" }}>{message}</p>}
        <table
          border={1}
          cellPadding={10}
          style={{ width: "100%", textAlign: "left" }}
        >
          <thead>
            <tr>
              <th></th>
              <th>사번</th>
              <th>이름</th>
              <th>지점</th>
              <th>직급</th>
              <th>권한</th>
              <th>상태</th>
              <th>정보 수정</th>
              <th>퇴사 처리</th>
            </tr>
          </thead>
          <tbody>
            {paginatedEmployees.map((emp, index) => (
              <tr key={emp.employeeId}>
                <td>{currentPage * ITEMS_PER_PAGE + index + 1}</td>
                <td>{emp.employeeNumber}</td>
                <td>{emp.employeeName}</td>
                <td>{emp.branchName}</td>
                <td>{emp.positionName}</td>
                <td>{emp.authorityName}</td>
                <td>{emp.status === "EXITED" ? "퇴사" : "재직"}</td>
                <td>
                  <button
                    onClick={() => onOpenModalUpdateClick(emp)}
                    className="approval-button"
                  >
                    정보 수정
                  </button>
                </td>
                <td>
                  <button
                    onClick={() => onOpenModalExitUpdateClick(emp)}
                    className="approval-button"
                  >
                    퇴사 처리
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {totalPages > 1 && (
          <div style={{ marginTop: "20px" }}>
            <button
              onClick={() => setCurrentPage((prev) => Math.max(prev - 1, 1))}
              disabled={currentPage === 1}
            >
              이전
            </button>
            <span style={{ margin: "0 10px" }}>
              {currentPage} / {totalPages}
            </span>
            <button
              onClick={() =>
                setCurrentPage((prev) => Math.min(prev + 1, totalPages))
              }
              disabled={currentPage === totalPages}
            >
              다음
            </button>
          </div>
        )}
        {employeeList.length > 0 && (
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
        {modalStatus && (
          <Modal
            isOpen={modalStatus}
            onClose={() => setModalStatus(false)}
            children={modalContent}
          />
        )}
        {modalExitStatus && (
          <Modal
            isOpen={modalExitStatus}
            onClose={() => setModalExitStatus(false)}
            children={modalExitContent}
          />
        )}
      </div>
    </>
  );
}

export default EmployeeChange;
