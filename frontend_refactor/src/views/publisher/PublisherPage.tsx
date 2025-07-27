// src/views/publisher/PublisherPage.tsx
/** @jsxImportSource @emotion/react */
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import CreatePublisherModalLauncher from "./CreatePublisherModalLauncher";
import UpdatePublisher from "./UpdatePublisher";
import * as style from "@/styles/style";
import { getPublishers, deletePublisher } from "@/apis/publisher/publisher";
import { PublisherResponseDto } from "@/dtos/publisher/response/publisher.response.dto";
import "./publisher.css";
import usePagination from "@/hooks/usePagination";
import Pagination from "@/components/Pagination";
import DataTable from "@/components/Table";
function PublisherPage() {
  const [cookies] = useCookies(["accessToken"]);
  const accessToken = cookies.accessToken;
  const [searchForm, setSearchForm] = useState({ publisherName: "" });
  const [message, setMessage] = useState("");
  const [publishers, setPublishers] = useState<PublisherResponseDto[]>([]);

  const pageSize = 10;

  // 수정 모달 상태
  const [selectedPublisher, setSelectedPublisher] =
    useState<PublisherResponseDto | null>(null);
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
      console.error("목록 조회 중 예외:", err);
    }
  };

  // 삭제 기능
  const onDelete = async (id: number) => {
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    if (!accessToken) return;
    try {
      const response = await deletePublisher(id, accessToken);
      if (response.code === "SU") {
        // 삭제 후 빈 페이지라면 이전 페이지로
        if (publishers.length === 1 && currentPage > 0) {
          fetchPage(searchForm.publisherName);
        } else {
          fetchPage(searchForm.publisherName);
        }
      } else {
        alert(response.message || "삭제 중 오류");
      }
    } catch (err) {
      console.error("삭제 중 예외:", err);
      alert("삭제 중 오류가 발생했습니다.");
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
    setSearchForm({ publisherName: "" });
    fetchPage("");
  };

  return (
    <>
      <div className="filter-bar">
        <div className="left-item">
        <CreatePublisherModalLauncher
          onCreated={() => fetchPage(searchForm.publisherName)}
          />
          </div>
        
          <input
            type="text"
            name="publisherName"
            value={searchForm.publisherName}
            placeholder="검색할 출판사 이름을 입력하세요."
            onChange={onSearchInputChange}
          />
          <button
            onClick={() => fetchPage(searchForm.publisherName)}
          >
            검색
          </button>
          <button className="searchAllButton" onClick={onSearchAll}>
            전체 조회
          </button>
      </div>

      <DataTable<PublisherResponseDto>
        columns={[
          {
            header: "번호",
            accessor: "number",
            cell: (_, index) => currentPage * pageSize + index + 1,
          },
          { header: "출판사 이름", accessor: "publisherName" },
        ]}
        data={pagedPublishers}
        actions={[
          {
            label: "수정",
            onClick: openUpdateModal,
            buttonCss: style.modifyButton,
          },
          {
            label: "삭제",
            onClick: (pub) => onDelete(pub.publisherId),
            buttonCss: style.deleteButton,
          },
        ]}
      />
      {message && <p>{message}</p>}

      {pagedPublishers.length > 0 && (
        <Pagination
          currentPage={currentPage}
          totalPages={totalPages}
          onPageChange={goToPage}
          onPrev={goPrev}
          onNext={goNext}
        />
      )}

      {isUpdateOpen && selectedPublisher && (
        <UpdatePublisher
          publisher={selectedPublisher}
          onClose={handleUpdateClose}
          onUpdated={handleUpdated}
        />
      )}
    </>
  );
}

export default PublisherPage;
