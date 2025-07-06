import { branchSearchRequest } from "@/apis/branch/branch";
import { BranchSearchResponseDto } from "@/dtos/branch/response/branch-search.respnse.dto";
import { tr } from "date-fns/locale";
import React, { useState } from "react";
import { useCookies } from "react-cookie";

const ITEMS_PAGE = 10;

function BranchSearch() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({ branchLocation: "" });
  const [branchList, setBranchList] = useState<BranchSearchResponseDto[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const totalPages = Math.ceil(branchList.length / ITEMS_PAGE);

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

    const response = await branchSearchRequest(searchForm, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setBranchList(data);
    } else {
      setBranchList([]);
    }

    setCurrentPage(0);
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
      <h2>지점 조회</h2>
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
        </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>지점 명</th>
            <th>지점 주소</th>
            <th>등록 일자</th>
          </tr>
        </thead>
        <tbody>
          {paginatedBranchList.map((branch) => (
            <tr key={branch.branchId}>
              <td>{branch.branchName}</td>
              <td>{branch.branchLocation}</td>
              <td>{new Date(branch.createdAt).toLocaleString()}</td>
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
    </div>
  );
}

export default BranchSearch;
