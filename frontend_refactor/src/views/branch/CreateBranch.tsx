/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import {
  branchCreateRequest,
  branchDetailRequest,
  branchSearchRequest,
  branchUpdateRequest,
} from "@/apis/branch/branch";
import Modal from "@/components/Modal";
import { BranchSearchResponseDto } from "@/dtos/branch/response/branch-search.respnse.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import "@/styles/style.css";
import DataTable from "@/components/Table";
import Pagination from "@/components/Pagination";
import usePagination from "@/hooks/usePagination";

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
    const { code, data } = response;

    if (code === "SU" && data) {
      setBranchList(data);
    } else {
      setBranchList([]);
    }
  };

  const onResetClick = () => {
    setSearchForm({ branchLocation: "" });
    setBranchList([]);
  };

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

  const {
    currentPage,
    totalPages,
    pagedItems: paginatedBranchList,
    goToPage,
    goPrev,
    goNext,
  } = usePagination(branchList, 10);

  return (
    <div className="searchContainer">
      <h2>지점 관리</h2>
      <div className="filter-bar">
        <input
          type="text"
          name="branchLocation"
          value={searchForm.branchLocation}
          placeholder="지점 주소"
          onChange={onInputChange}
        />
        <button onClick={onSearchClick}>검색</button>
        <button onClick={onResetClick}>초기화</button>
        <button onClick={onOpenCreateModal}>등록</button>
      </div>

      <DataTable<BranchSearchResponseDto>
        columns={[
          { header: "지점 명", accessor: "branchName" },
          { header: "지점 주소", accessor: "branchLocation" },
          {
            header: "등록 일자",
            accessor: "createdAt",
            cell: (item) => new Date(item.createdAt).toLocaleString(),
          },
        ]}
        data={paginatedBranchList}
        actions={[
          {
            label: "수정",
            onClick: onOpenUpdateModal,
            buttonCss: style.modifyButton,
          },
        ]}
      />

      {branchList.length > 0 && (
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
