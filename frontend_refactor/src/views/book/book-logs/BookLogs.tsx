import { useState } from "react";
import { useCookies } from "react-cookie";
import "./../book.css";
import { BookLogResponseDto } from "@/dtos/book/response/book-log-response.dto";
import { getBookLogs } from "@/apis/book/book";

function BookLogs() {
  const [cookies] = useCookies(["accessToken"]);
  const [isbn, setIsbn] = useState("");
  const [logs, setLogs] = useState<BookLogResponseDto[]>([]);
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 10;

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
      setCurrentPage(0);
    } catch (error) {
      alert("도서 로그 조회 실패");
      console.error(error);
    }
  };

  const totalPages = Math.ceil(logs.length / itemsPerPage);
  const pagesPerGroup = 5;
  const currentGroup = Math.floor(currentPage / pagesPerGroup);
  const startPage = currentGroup * pagesPerGroup;
  const endPage = Math.min(startPage + pagesPerGroup, totalPages);

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

  const logsToDisplay = logs.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  return (
    <div>
      <div className="topBar">
        <h2>📘 도서 로그 조회</h2>
        <input
          className="book-input"
          value={isbn}
          onChange={(e) => setIsbn(e.target.value)}
          placeholder="ISBN을 입력하세요"
        />
        <button type="button" className="button" onClick={handleSearch}>
          검색
        </button>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>도서명</th>
              <th>로그유형</th>
              <th>이전 가격</th>
              <th>이전 할인율</th>
              <th>담당자</th>
              <th>변경일</th>
            </tr>
          </thead>
          <tbody>
            {logsToDisplay.map((log) => (
              <tr key={log.bookLogId}>
                <td>{log.bookTitle}</td>
                <td>{log.bookLogType}</td>
                <td>{log.previousPrice ?? "-"}</td>
                <td>{log.previousDiscountRate ?? "-"}</td>
                <td>{log.employeeName}</td>
                <td>{log.changedAt ? new Date(log.changedAt).toLocaleDateString() : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {logs.length > 0 && (
        <div className="footer">
          <button className="pageBtn" onClick={goPrev} disabled={currentPage === 0}>
            {"<"}
          </button>
          {Array.from({ length: endPage - startPage }, (_, i) => startPage + i).map((i) => (
            <button
              key={i}
              className={`pageBtn${i === currentPage ? " current" : ""}`}
              onClick={() => goToPage(i)}
            >
              {i + 1}
            </button>
          ))}
          <button className="pageBtn" onClick={goNext} disabled={currentPage >= totalPages - 1}>
            {">"}
          </button>

          <span className="pageText">{`${currentPage + 1} / ${totalPages}`}</span>
        </div>
      )}
    </div>
  );
}

export default BookLogs;
