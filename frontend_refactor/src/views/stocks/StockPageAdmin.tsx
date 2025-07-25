import { searchStocks, searchStocksByBranch } from "@/apis/stock/stock";
import { StockListResponseDto } from "@/dtos/stock/Stock.response.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";

function StockPageAdmin() {
  const [cookies] = useCookies(["accessToken"]);

  const [stocks, setStocks] = useState<StockListResponseDto[]>([]);

  const [searchForm, setSearchForm] = useState({
    bookTitle: "",
    isbn: "",
    branchName: "",
  });

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const fetchStocks = async () => {
    setStocks([]);
    const { bookTitle, isbn, branchName } = searchForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await searchStocks(bookTitle, isbn, branchName, token);

    const { code, message, data } = response;

    if (!code) {
      alert(message);
      return;
    }

    if (Array.isArray(data)) {
      setStocks(data);

    } else {
      alert("올바른 검색 조건을 입력해주세요.");
    }
  };
  return (
    <div className="stock-page-container">
      <div className="topBar">
        <div
          style={{
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
            width: "100%",
          }}
        >
          <div style={{ display: "flex", gap: 12, height: 35 }}>
            <input
              type="text"
              name="bookTitle"
              value={searchForm.bookTitle}
              placeholder="책 제목"
              onInput={onInputChange}
              style={{ border: "1px solid #ccc", textAlign: "center" }}
            />
            <input
              type="text"
              name="isbn"
              value={searchForm.isbn}
              placeholder="ISBN"
              onInput={onInputChange}
              style={{ border: "1px solid #ccc", textAlign: "center" }}
            />
            <input
              type="text"
              name="branchName"
              value={searchForm.branchName}
              placeholder="지점명"
              onInput={onInputChange}
              style={{ border: "1px solid #ccc", textAlign: "center" }}
            />
            <button className="modifyBtn" onClick={fetchStocks}>
              검색
            </button>
          </div>
        </div>
      </div>

      <div className="table-container margin-top">
        <table>
          <thead>
            <tr>
              <th>지점</th>
              <th>책 제목</th>
              <th>수량</th>
            </tr>
          </thead>
          <tbody>
            {stocks.length === 0 ? (
              <tr>
                <td colSpan={5} className="gray-text">
                  데이터가 없습니다.
                </td>
              </tr>
            ) : (
              stocks.map((stock) => (
                <tr key={stock.stockId}>
                  <td>{stock.branchName}</td>
                  <td>{stock.bookTitle}</td>
                  <td>{stock.amount}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}

export default StockPageAdmin;
