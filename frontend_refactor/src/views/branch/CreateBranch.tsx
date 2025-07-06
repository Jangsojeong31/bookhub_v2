import {
  branchCreateRequest,
  branchDetailRequest,
  branchSearchRequest,
  branchUpdateRequest,
} from "@/apis/branch/branch";
import Modal from "@/apis/constants/Modal";
import { BranchSearchResponseDto } from "@/dtos/branch/response/branch-search.respnse.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";

const ITEMS_PAGE = 10;

function CreateBranch() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({ branchLocation: "" });
  const [branchList, setBranchList] = useState<BranchSearchResponseDto[]>([]);
  const [branchDetail, setBranchDetail] = useState({
    branchId: 0,
    branchName: "",
    branchLocation: "",
  });
  const [currentPage, setCurrentPage] = useState(0);
  const totalPages = Math.ceil(branchList.length / ITEMS_PAGE);
  const [createBranch, setCreateBranch] = useState({
    branchName: "",
    branchLocation: "",
  });

  const [modalStatus, setModalStatus] = useState(false);
  const [modalUpdateStatus, setModalUpdateStatus] = useState(false);

  const onInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const onCreateInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setCreateBranch({ ...createBranch, [name]: value });
  };

  const onUpdateInputChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setBranchDetail({ ...branchDetail, [name]: value });
  };

  const onSearchClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await branchSearchRequest(searchForm, token);
    const { code, message, data } = response;

    setCurrentPage(0);
    if (code === "SU" && data) {
      setBranchList(data);
    } else {
      setBranchList([]);
    }
  };

  const onResetClick = () => {
    setSearchForm({ branchLocation: "" });
    setBranchList([]);
    setCurrentPage(0);
  };

  const paginatedBranchList = branchList.slice(
    currentPage * ITEMS_PAGE,
    (currentPage + 1) * ITEMS_PAGE
  );

  const onOpenCreateModal = () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    setCreateBranch({
      branchName: "",
      branchLocation: "",
    });

    setModalStatus(true);
  };

  const onCreateClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await branchCreateRequest(createBranch, token);
    const { code, message } = response;

    if (code === "SU") {
      alert("지점이 등록되었습니다.");
    } else {
      alert(message);
      return;
    }

    setModalStatus(false);
  };

  const modalContent = (
    <>
      <div className="contain">
        <h1>지점 등록</h1>
        <input
          type="text"
          name="branchName"
          value={createBranch.branchName}
          placeholder="지점 명"
          onChange={onCreateInputChange}
          className="de-input"
        />
        <input
          type="text"
          name="branchLocation"
          value={createBranch.branchLocation}
          placeholder="지점 위치"
          onChange={onCreateInputChange}
          className="de-input"
        />
        <button onClick={onCreateClick} className="de-button">
          등록
        </button>
      </div>
    </>
  );

  const onOpenUpdateModal = async (branch: BranchSearchResponseDto) => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await branchDetailRequest(branch.branchId, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setBranchDetail(data);
    } else {
      alert(message);
      return;
    }
    setModalUpdateStatus(true);
  };

  const modalUpdateContent = (
    <>
      <div className="contain">
        <h1>지점 수정</h1>
        <input
          type="text"
          name="branchName"
          value={branchDetail.branchName}
          placeholder="지점 명"
          onChange={onUpdateInputChange}
          className="de-input"
        />
        <input
          type="text"
          name="branchLocation"
          value={branchDetail.branchLocation}
          placeholder="지점 위치"
          onChange={onUpdateInputChange}
          className="de-input"
        />
        <button
          onClick={() => onUpdateClick(branchDetail.branchId)}
          className="de-button"
        >
          수정
        </button>
      </div>
    </>
  );

  const onUpdateClick = async (branchId: number) => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await branchUpdateRequest(branchId, branchDetail, token);
    const { code, message } = response;

    if (code === "SU") {
      alert("지점이 수정되었습니다.");
    } else {
      alert(message);
      return;
    }

    setModalUpdateStatus(false);
    onSearchClick();
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

  return (
    <div className="searchContainer">
      <h2>지점 관리</h2>
      <div className="search-row">
        <input
          type="text"
          name="branchLocation"
          value={searchForm.branchLocation}
          placeholder="지점 주소"
          onChange={onInputChange}
        />
        <div className="search-button">
          <button onClick={onSearchClick}>검색</button>
          <button onClick={onResetClick}>최기화</button>
          <button style={{ float: "right" }} onClick={onOpenCreateModal}>
            등록
          </button>
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>지점 명</th>
            <th>지점 주소</th>
            <th>등록 일자</th>
            <th>지점 수정</th>
          </tr>
        </thead>
        <tbody>
          {paginatedBranchList.map((branch) => (
            <tr key={branch.branchId}>
              <td>{branch.branchName}</td>
              <td>{branch.branchLocation}</td>
              <td>{new Date(branch.createdAt).toLocaleString()}</td>
              <td>
                <button
                  onClick={() => onOpenUpdateModal(branch)}
                  className="approval-button"
                >
                  수정
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      {branchList.length > 0 && (
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
      {modalUpdateStatus && (
        <Modal
          isOpen={modalUpdateStatus}
          onClose={() => setModalUpdateStatus(false)}
          children={modalUpdateContent}
        />
      )}
    </div>
  );
}

export default CreateBranch;
