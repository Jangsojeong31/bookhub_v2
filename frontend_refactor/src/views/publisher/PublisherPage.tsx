// src/views/publisher/PublisherPage.tsx
/** @jsxImportSource @emotion/react */
import React, { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import CreatePublisherModalLauncher from './CreatePublisherModalLauncher';
import UpdatePublisher from './UpdatePublisher';
import * as style from "@/styles/style";
import {
  getPublishers,
  deletePublisher
} from '@/apis/publisher/publisher';
import { PublisherResponseDto } from '@/dtos/publisher/response/publisher.response.dto';
import './publisher.css';
import usePagination from '@/hooks/usePagination';
import Pagination from '@/components/Pagination';
function PublisherPage() {

  const [cookies] = useCookies(['accessToken']);
  const accessToken = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({ publisherName: "" });
  const [message, setMessage] = useState("");

  const pageSize = 10;

  // 현재 화면에 보여줄 출판사 리스트
  const [publishers, setPublishers] = useState<PublisherResponseDto[]>([]);

  // 수정 모달 상태
  const [selectedPublisher, setSelectedPublisher] = useState<PublisherResponseDto | null>(null);
  const [isUpdateOpen, setIsUpdateOpen] = useState<boolean>(false);

  const onSearchInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
      const { name, value } = e.target;
      setSearchForm({ ...searchForm, [name]: value });
    };

  const fetchPage = async (publisherName: string) => {
    if (!accessToken) return;
  
    try {
      const response = await getPublishers(accessToken, publisherName);
      const { code, message, data } = response;
      if (code != "SU") {
      setPublishers([]);
      setMessage(message);
      return;
    }

    if (Array.isArray(data)) {
      setPublishers(data);
      setMessage("");
    } else {
      setPublishers([]);
      setMessage("검색 결과가 없습니다.");
    }
      
    } catch (err) {
      console.error('목록 조회 중 예외:', err);
    }
  };

  // 삭제 기능
  const onDelete = async (id: number) => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return;
    if (!accessToken) return;
    try {
      const response = await deletePublisher(id, accessToken);
      if (response.code === 'SU') {
        // 삭제 후 빈 페이지라면 이전 페이지로
        if (publishers.length === 1 && currentPage > 0) {
          fetchPage(searchForm.publisherName);
        } else {
          fetchPage(searchForm.publisherName);
        }
      } else {
        alert(response.message || '삭제 중 오류');
      }
    } catch (err) {
      console.error('삭제 중 예외:', err);
      alert('삭제 중 오류가 발생했습니다.');
    }
  };

  // 수정 모달
  const openUpdateModal = (pub: PublisherResponseDto) => {
    setSelectedPublisher(pub);
    setIsUpdateOpen(true);
  };
  const handleUpdateClose = () => {
    setSelectedPublisher(null);
    setIsUpdateOpen(false);
  };
  const handleUpdated = async () => {
    handleUpdateClose();
    await fetchPage(searchForm.publisherName);
  };


   const {
      currentPage,
      totalPages,
      pagedItems: pagedPublishers,
      goToPage,
      goPrev,
      goNext,
    } = usePagination(publishers, 10);

  // 전체조회 (검색어 초기화)
  const onSearchAll = () => {
    setSearchForm({publisherName: ""});
    fetchPage("");
  };

  return (
    <div className="publisher-page-container">
      {/* 상단: 등록 버튼 + 검색창 */}
      <div className="topBar">
        <CreatePublisherModalLauncher onCreated={() => fetchPage(searchForm.publisherName)} />
                  <input
                            type="text"
                            name="publisherName"
                            value={searchForm.publisherName}
                            placeholder="검색할 출판사 이름을 입력하세요."
                            onChange={onSearchInputChange}
                            // onKeyDown={handleKeyDown}
                            css={style.searchInput}
                            />
                  <button onClick={() => fetchPage(searchForm.publisherName)}>검색</button>
      </div>

      {/* 테이블 */}
      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th style={{ width: '60px' }}>번호</th>
              <th>출판사 이름</th>
              <th style={{ width: '120px' }}>작업</th>
            </tr>
          </thead>
          <tbody>
            {publishers.length === 0 ? (
              <tr>
                <td colSpan={3} className="gray-text">출판사 정보가 없습니다.</td>
              </tr>
            ) : (
              pagedPublishers.map((pub, idx) => (
                <tr key={pub.publisherId} className="tableheight">
                  <td>{currentPage * pageSize + idx + 1}</td>
                  <td>{pub.publisherName}</td>
                  <td>
                    <button className="modifyBtn" onClick={() => openUpdateModal(pub)}>수정</button>
                    <button className="deleteBtn" onClick={() => onDelete(pub.publisherId)}>삭제</button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
        {message && <p>{message}</p>}
      </div>

      {pagedPublishers.length > 0 && (
              <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={goToPage}
              onPrev={goPrev}
              onNext={goNext}
              />
            )}
        <button className="searchAll" onClick={onSearchAll}>전체 조회</button>

      {isUpdateOpen && selectedPublisher && (
        <UpdatePublisher
          publisher={selectedPublisher}
          onClose={handleUpdateClose}
          onUpdated={handleUpdated}
        />
      )}
    </div>
  );
};

export default PublisherPage;
