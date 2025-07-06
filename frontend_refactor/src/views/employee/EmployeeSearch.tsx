import { GET_BRANCH_URL } from "@/apis";
import Modal from "@/apis/constants/Modal";
import {
  employeeDetailRequest,
  employeeRequest,
} from "@/apis/employee/employee";
import { EmployeeDetailResponseDto } from "@/dtos/employee/response/employee-detail.response.dto";
import { EmployeeListResponseDto } from "@/dtos/employee/response/employee-list.response.dto";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/employee/employeeSelect.css";
import "@/styles/employee/employeemodal.css";

const positionOptions = ["사원", "대리", "과장", "부장", "점장"];
const authorityOptions = ["STAFF", "MANAGER", "ADMIN"];
const statusOptions = ["EMPLOYED", "EXITED"];
const ITEMS_PER_PAGE = 10;

interface Branch {
  branchId: number;
  branchName: string;
  branchLocation: string;
}

function EmployeeSearch() {
  const [searchForm, setSearchForm] = useState({
    name: "",
    branchName: "",
    positionName: "",
    authorityName: "",
    status: "",
  });

  const [currentPage, setCurrentPage] = useState(0);
  const [cookies] = useCookies(["accessToken"]);
  const [employeeList, setEmployeeList] = useState<EmployeeListResponseDto[]>(
    []
  );
  const [message, setMessage] = useState("");
  const [branches, setBranches] = useState<Branch[]>([]);
  const [modalStatus, setModalStatus] = useState(false);
  const [employee, setEmployee] = useState<EmployeeDetailResponseDto>();

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
  }, []);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const onSearchClick = async () => {
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeRequest(searchForm, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setEmployeeList(data);
      setMessage("");
    } else {
      setEmployeeList([]);
      setMessage(message);
    }

    setCurrentPage(0);
  };

  const paginatedEmployees = employeeList.slice(
    currentPage * ITEMS_PER_PAGE,
    (currentPage + 1) * ITEMS_PER_PAGE
  );

  const onResetClick = () => {
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

  const onOpenModalClick = async (employee: EmployeeListResponseDto) => {
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await employeeDetailRequest(employee.employeeId, token);
    const { code, message, data } = response;

    if (code === "SU") {
      setEmployee(data);
    } else {
      setMessage(message);
      return;
    }

    setModalStatus(true);
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
              <span>{employee?.employeeNumber}</span>
            </div>
            <div className="field">
              <label>사원 이름</label>
              <span>{employee?.employeeName}</span>
            </div>
            <div className="field">
              <label>지점 명</label>
              <span>{employee?.branchName}</span>
            </div>
            <div className="field">
              <label>지급 명</label>
              <span>{employee?.positionName}</span>
            </div>
            <div className="field">
              <label>권한 명</label>
              <span>{employee?.authorityName}</span>
            </div>
          </div>
          <div className="column">
            <div className="field">
              <label>이메일</label>
              <span>{employee?.email}</span>
            </div>
            <div className="field">
              <label>전화 번호</label>
              <span>{employee?.phoneNumber}</span>
            </div>
            <div className="field">
              <label>생년월일: </label>
              <span>
                {new Date(employee?.birthDate || "").toLocaleDateString()}
              </span>
            </div>
            <div className="field">
              <label>재직 상태</label>
              <span>{employee?.status === "EXITED" ? "퇴사" : "재직"}</span>
            </div>
            <div className="field">
              <label>입사 일자:</label>
              <span>
                {new Date(employee?.createdAt || "").toLocaleString()}
              </span>
            </div>
          </div>
        </div>
      </div>
    </>
  );

  return (
    <div>
      <div className="searchContainer">
        <h2>사원 정보 조회</h2>
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
            {positionOptions.map((p) => (
              <option key={p} value={p}>
                {p}
              </option>
            ))}
          </select>

          <select
            name="authorityName"
            value={searchForm.authorityName}
            onChange={onInputChange}
          >
            <option value="">권한 선택</option>
            {authorityOptions.map((a) => (
              <option key={a} value={a}>
                {a}
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
            <button onClick={onResetClick}>초기화</button>
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
            <th>세부 사항</th>
          </tr>
        </thead>
        <tbody>
          {paginatedEmployees.map((emp, index) => (
            <tr key={emp.employeeId}>
              <th>{currentPage * ITEMS_PER_PAGE + index + 1}</th>
              <td>{emp.employeeNumber}</td>
              <td>{emp.employeeName}</td>
              <td>{emp.branchName}</td>
              <td>{emp.positionName}</td>
              <td>{emp.authorityName}</td>
              <td>{emp.status === "EXITED" ? "퇴사" : "재직"}</td>
              <td>
                <button
                  onClick={() => onOpenModalClick(emp)}
                  className="approval-button"
                >
                  세부 사항
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

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
    </div>
  );
}

export default EmployeeSearch;
