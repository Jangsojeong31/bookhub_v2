/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import { useState } from "react";
import { useCookies } from "react-cookie";
import "./../book.css";
import { BookLogResponseDto } from "@/dtos/book/response/book-log-response.dto";
import { getBookLogs } from "@/apis/book/book";
import Pagination from "@/components/Pagination";
import usePagination from "@/hooks/usePagination";
import DataTable from "@/components/Table";

function BookLogs() {
  const [cookies] = useCookies(["accessToken"]);
  const [isbn, setIsbn] = useState("");
  const [logs, setLogs] = useState<BookLogResponseDto[]>([]);

  const handleSearch = async () => {
    const token = cookies.accessToken;
    if (!isbn.trim()) {
      alert("ISBN을 입력해주세요.");
      return;
    }

    try {
      const res = await getBookLogs(isbn, token);
      if (res.code !== "SU") throw new Error(res.message);
      setLogs(res.data || []);
    } catch (error) {
      alert("도서 로그 조회 실패");
      console.error(error);
    }
  };

  const {
    currentPage,
    totalPages,
    pagedItems: pagedLogs,
    goToPage,
    goPrev,
    goNext,
  } = usePagination(logs, 10);

  return (
    <div>
      <div className="topBar">
        <h2 css={style.pageTitle}>도서 로그 조회</h2>
        <div css={style.searchContainer}>
          <input
            css={style.searchInput}
            value={isbn}
            onChange={(e) => setIsbn(e.target.value)}
            placeholder="ISBN을 입력하세요"
          />
          <button type="button" css={style.searchButton} onClick={handleSearch}>
            검색
          </button>
        </div>
      </div>

      <div className="table-container">
        <DataTable<BookLogResponseDto>
        columns={[
          { header: "도서명", accessor: "bookTitle" },
          { header: "로그 유형", accessor: "bookLogType" },
          { header: "이전 가격", accessor: "previousPrice" },
          { header: "이전 할인율", accessor: "previousDiscountRate" },
          { header: "담당자", accessor: "employeeName" },
          { header: "변경일", accessor: "changedAt" },
        ]}
        data={pagedLogs}
      />
      </div>

      {logs.length > 0 && (
        <Pagination
                  currentPage={currentPage}
                  totalPages={totalPages}
                  onPageChange={goToPage}
                  onPrev={goPrev}
                  onNext={goNext}
                />
      )}
    </div>
  );
}

export default BookLogs;
