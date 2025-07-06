

import React, { useState, useEffect, useCallback } from 'react';
import { useCookies } from 'react-cookie';
import {
  getStockByIsbn,
  getStockByTitle,
  getStockByBranch,
} from '@/apis/stock/stock';
import { StockListResponseDto } from '@/dtos/stock/Stock.response.dto';
import StockUpdateModal from './StockUpdateModal';
import './stockmodal.css';

function StockPage () {
  const [cookies] = useCookies(['accessToken']);
  const accessToken = cookies.accessToken as string | undefined;

  const [stocks, setStocks] = useState<StockListResponseDto[]>([]);
  const [searchMode, setSearchMode] = useState<'bookIsbn' | 'title' | 'branch' | ''>('');
  const [searchValue, setSearchValue] = useState<string>('');
  const [branchId, setBranchId] = useState<number | ''>('');

  const [selectedStock, setSelectedStock] = useState<StockListResponseDto | null>(null);
  const [isUpdateOpen, setIsUpdateOpen] = useState<boolean>(false);

  // Mode Change시 Data clear
  useEffect(() => {
    setStocks([]);
    setSearchValue('');
    setBranchId('');
  }, [searchMode]);

  // 현재 Mode에 따른 데이터 
  const fetchStocks = useCallback(async () => {
    if (!accessToken || !searchMode) return;

    try {
      let response;

      switch (searchMode) {
        case 'bookIsbn':
          if (!searchValue.trim()) {
            alert('ISBN을 입력해주세요.');
            return;
          }
          response = await getStockByIsbn(searchValue.trim(), accessToken);
          break;

        case 'title':
          if (!searchValue.trim()) {
            alert('제목 키워드를 입력해주세요.');
            return;
          }
          response = await getStockByTitle(searchValue.trim(), accessToken);
          break;

        case 'branch':
          if (branchId === '' || branchId <= 0) {
            alert('올바른 지점 ID를 입력해주세요.');
            return;
          }
          response = await getStockByBranch(branchId as number, accessToken);
          break;

        default:
          return;
      }

      if (response?.code === 'SU' && response.data) {
        setStocks(response.data);
      } else {
        setStocks([]);
      }
    } catch (error) {
      console.error(error);
      alert('데이터 조회 중 오류가 발생했습니다.');
      setStocks([]);
    }
  }, [accessToken, searchMode, searchValue, branchId]);

  // Handlers
  const handleModeChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSearchMode(e.target.value as 'bookIsbn' | 'title' | 'branch' | '');
  };

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchValue(e.target.value);
  };

  const handleBranchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = Number(e.target.value);
    setBranchId(isNaN(value) ? '' : value);
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
    fetchStocks();
  };

  return (
    <div className="stock-page-container">
      {/* Search Mode Selector */}
      <div className="topBar">
        <select value={searchMode} onChange={handleModeChange}>
          <option value="">— 검색 모드 선택 —</option>
          <option value="bookIsbn">ISBN</option>
          <option value="title">제목</option>
          <option value="branch">지점</option>
        </select>
      </div>

      {searchMode && (
        <>
          {/* Search Input and Button */}
          <div className="topBar margin-top">
            {searchMode !== 'branch' ? (
              <input
                className="search"
                placeholder={
                  searchMode === 'bookIsbn' ? 'ISBN 입력' : '제목 키워드 입력'
                }
                value={searchValue}
                onChange={handleSearchChange}
              />
            ) : (
              <input
                className="search"
                type="number"
                placeholder="지점 ID 입력"
                value={branchId}
                onChange={handleBranchChange}
              />
            )}
            <button className = "modifyBtn" onClick={fetchStocks}>검색</button>
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
                        <button className = "modifyBtn" onClick={() => openUpdateModal(stock)}>
                          수정
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </>
      )}

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
};

export default StockPage;
