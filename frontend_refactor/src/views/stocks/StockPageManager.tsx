import React, { useState, useEffect, useCallback } from "react";
import { useCookies } from "react-cookie";
import { searchStocks, searchStocksByBranch } from "@/apis/stock/stock";
import { StockListResponseDto } from "@/dtos/stock/Stock.response.dto";
import StockUpdateModal from "./StockUpdateModal";
import "./stockmodal.css";
import { useEmployeeStore } from "@/stores/useEmployeeStore";

function StockPage() {
  const [cookies] = useCookies(["accessToken"]);

  const [stocks, setStocks] = useState<StockListResponseDto[]>([]);
  const [selectedStock, setSelectedStock] =
    useState<StockListResponseDto | null>(null);
  const [isUpdateOpen, setIsUpdateOpen] = useState<boolean>(false);
  const branchName = useEmployeeStore((state) => state.employee?.branchName);
  // const [branchName, setBranchName] = useState("");

  const [searchForm, setSearchForm] = useState({
    bookTitle: "",
    isbn: "",
  });

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  useEffect(() => {
    searchStocks();
  }, [])

  const searchStocks = async () => {
    setStocks([]);
    const { bookTitle, isbn } = searchForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await searchStocksByBranch(bookTitle, isbn, token);

    const { code, message, data } = response;

    if (!code) {
      alert(message);
      return;
    }

    if (Array.isArray(data)) {
      setStocks(data);
      // setMessage("");
    } else {
      alert("올바른 검색 조건을 입력해주세요.");
    }
  };

  const openUpdateModal = (stock: StockListResponseDto) => {
    setSelectedStock(stock);
    setIsUpdateOpen(true);
  };

  const closeUpdateModal = () => {
    setIsUpdateOpen(false);
    setSelectedStock(null);
  };

  const handleUpdated = () => {
    closeUpdateModal();
    searchStocks();
  };

  return (
    <div className="stock-page-container">
                <h2>재고 관리 [ {branchName} ]</h2>

      <div className="topBar">
        {/* <div style={{ display: "flex", gap: 12 }}> */}
        <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", width: "100%"}}>
          <div style={{display: "flex", gap: 12, height: 35}}>
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
          <button className="modifyBtn" onClick={searchStocks}>
            검색
          </button>
          </div>
        
        
          </div>
      </div>

      {/* Results Table */}
      <div className="table-container margin-top">
        <table>
          <thead>
            <tr>
              <th>Stock ID</th>
              <th>지점</th>
              <th>책 제목</th>
              <th>수량</th>
              <th>작업</th>
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
                  <td>{stock.stockId}</td>
                  <td>{stock.branchName}</td>
                  <td>{stock.bookTitle}</td>
                  <td>{stock.amount}</td>
                  <td>
                    <button
                      className="modifyBtn"
                      onClick={() => openUpdateModal(stock)}
                    >
                      수정
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Update Modal */}
      {isUpdateOpen && selectedStock && (
        <StockUpdateModal
          stock={selectedStock}
          onClose={closeUpdateModal}
          onUpdated={handleUpdated}
        />
      )}
    </div>
  );
}

export default StockPage;
