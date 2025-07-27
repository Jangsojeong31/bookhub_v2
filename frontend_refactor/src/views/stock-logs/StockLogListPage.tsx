import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useCookies } from "react-cookie";
import { StockLogResponseDto } from "@/dtos/stock/StockLog.response.dto";
import { getStockLogsByBranch } from "@/apis/stock/stockLog";
import "./StockLogPage.css";
import { useEmployeeStore } from "@/stores/useEmployeeStore";

const StockLogListPage = () => {
  // const { branchId } = useParams();
  const navigate = useNavigate();
  const [cookies] = useCookies(["accessToken"]);
  const [logs, setLogs] = useState<StockLogResponseDto[]>([]);
  const branchName = useEmployeeStore((state) => state.employee?.branchName);
  const [searchForm, setSearchForm] = useState({
    type: "",
    bookIsbn: "",
    start: "",
    end: "",
  });

  useEffect(() => {
    onSearchLogs();
  }, []);

  const onSearchLogs = async () => {
    setLogs([]);
    const { type, bookIsbn, start, end } = searchForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await getStockLogsByBranch(
      type,
      bookIsbn,
      start,
      end,
      token
    );
    const { code, message, data } = response;

    if (!code) {
      alert(message);
      return;
    }

    if (Array.isArray(data)) {
      setLogs(data);
      // setMessage("");
    } else {
      alert("올바른 검색 조건을 입력해주세요.");
    }
  };

  return (
    <div className="stock-log-wrapper">

      <h2 className="stock-log-title">지점 재고로그 [ {branchName} ]</h2>
      <div className="filter-bar">
        <select
          name="isApproved"
          value={searchForm.type}
          onChange={(e) =>
            setSearchForm({
              ...searchForm,
              type: e.target.value,
            })
          }
        >
          <option value="">재고 이동 유형</option>
          <option value="IN">입고</option>
          <option value="OUT">출고</option>
          <option value="LOSS">분실/파손</option>
        </select>
        <input
          type="text"
          name="bookIsbn"
          value={searchForm.bookIsbn}
          placeholder="bookIsbn"
          onInput={(e: React.ChangeEvent<HTMLInputElement>) => {
            setSearchForm({ ...searchForm, bookIsbn: e.target.value });
          }}
        />
        <p>시작일</p>
        <input
          type="date"
          name="start"
          value={searchForm.start}
          placeholder="시작일"
          onInput={(e: React.ChangeEvent<HTMLInputElement>) => {
            setSearchForm({ ...searchForm, start: e.target.value });
          }}
        />
        <p>종료일</p>
        <input
          type="date"
          name="end"
          value={searchForm.end}
          placeholder="종료일"
          onInput={(e: React.ChangeEvent<HTMLInputElement>) => {
            setSearchForm({ ...searchForm, end: e.target.value });
          }}
        />
        <button onClick={onSearchLogs}>
          검색
        </button>
      </div>

      <table className="stock-log-table">
        <thead>
          <tr>
            <th>날짜</th>
            <th>유형</th>
            <th>도서명</th>
            <th>수량</th>
          </tr>
        </thead>
        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan={4} className="no-data">
                로그가 없습니다.
              </td>
            </tr>
          ) : (
            logs.map((log) => (
              <tr key={log.stockLogId}>
                <td>{log.actionDate}</td>
                <td>{log.type}</td>
                <td>{log.bookTitle}</td>
                <td>{log.amount}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
};

export default StockLogListPage;
