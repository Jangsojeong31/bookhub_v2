/** @jsxImportSource @emotion/react */
import React, { useState } from "react";
import {
  deleteAuthor,
  getAllAuthorsByName,
  updateAuthor,
} from "@/apis/author/author";
import { AuthorResponseDto } from "@/dtos/author/response/author.response.dto";
import { NavLink } from "react-router-dom";
import { useCookies } from "react-cookie";
import Modal from "@/components/Modal";
import * as style from "@/styles/style";
import CreateAuthor from "./CreateAuthor";

// & 기능: 이름으로 조회, 수정, 삭제

function ElseAuthor() {
  const [searchForm, setSearchForm] = useState({ authorName: "" });
  const [updateForm, setUpdateForm] = useState({
    authorName: "",
    authorEmail: "",
  });
  const [authorId, setAuthorId] = useState<number>(0);
  const [authors, setAuthors] = useState<AuthorResponseDto[]>([]);
  const [message, setMessage] = useState("");
  const [modalMessage, setModalMessage] = useState("");
  const [modalStatus, setModalStatus] = useState(false);
  const [cookies] = useCookies(["accessToken"]);

  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 10;

  const onSearchInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setSearchForm({ ...searchForm, [name]: value });
  };

  const onUpdateInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setUpdateForm({ ...updateForm, [name]: value });
  };

  // * 이름으로 조회
  const onGetAllAuthorsByNameClick = async () => {
    const { authorName } = searchForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }
    const response = await getAllAuthorsByName(authorName, token);
    const { code, message, data } = response;

    if (!code) {
      setMessage(message);
      return;
    }

    if (Array.isArray(data)) {
      setAuthors(data);
      setMessage("");
    } else {
      setMessage("데이터 형식이 올바르지 않습니다.");
    }

    setSearchForm({ authorName: "" });
  };

  // enter키 누르면 조회
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") onGetAllAuthorsByNameClick();
  };

  // * 수정 모달창
  const openUpdateModal = (author: AuthorResponseDto) => {
    setAuthorId(author.authorId);
    setUpdateForm({
      authorName: author.authorName,
      authorEmail: author.authorEmail,
    });
    setModalStatus(true);
  };

  // * 수정
  const onUpdateAuthorClick = async (authorId: number) => {
    setModalMessage("");
    const dto = {
      authorName: updateForm.authorName,
      authorEmail: updateForm.authorEmail,
    };
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    try {
      const updateResponse = await updateAuthor(authorId, dto, token);
      const { code: udpateCode, message } = updateResponse;

      if (udpateCode != "SU") {
        setMessage(message);

        return;
      }

      alert("수정되었습니다.");
      setModalStatus(false);
      const updatedAuthor = authors.map((author) =>
        author.authorId === authorId
          ? {
              ...author,
              authorName: updateForm.authorName,
              authorEmail: updateForm.authorEmail,
            }
          : author
      );
      setAuthors(updatedAuthor);
    } catch (error) {
      console.error(error);
      alert("수정 중 오류가 발생하였습니다.");
    }
  };

  // * 삭제
  const onDeleteAuthorClick = async (authorId: number) => {
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    // if (!window.confirm('정말 삭제하시겠습니까?')) return;

    try {
      const response = await deleteAuthor(authorId, token);
      const { code, message } = response;

      console.log(code);
      if (code != "SU") {
        setMessage(message);
        return;
      }

      alert("삭제되었습니다.");

      const updatedAuthor = authors.filter(
        (author) => author.authorId !== authorId
      );
      setAuthors(updatedAuthor);
    } catch (error) {
      console.error(error);
      alert("삭제 중 오류가 발생하였습니다.");
    }
  };

  // 페이지네이션
  const totalPages = Math.ceil(authors.length / itemsPerPage);

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

  const pagedAuthors = authors.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  const authorList = authors.map((author) => {
    return (
      <tr key={author.authorId}>
        <td>{author.authorName}</td>
        <td>{author.authorEmail}</td>
        <td>
          <button
            onClick={() => openUpdateModal(author)}
            css={style.modifyButton}
          >
            수정
          </button>
          <button
            onClick={() => onDeleteAuthorClick(author.authorId)}
            css={style.deleteButton}
          >
            삭제
          </button>
        </td>
      </tr>
    );
  });

  const modalContent: React.ReactNode = (
    <>
      <div style={{ display: "flex", justifyContent: "center", width: "100%" }}>
        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: 10,
            width: 500,
            height: 500,
            backgroundColor: "white",
            position: "absolute",
          }}
        >
          <h2 style={{ color: "#265185", textAlign: "center" }}>저자 수정</h2>

          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <p>저자 이름</p>
            <input
              type="text"
              name="authorName"
              value={updateForm.authorName}
              onChange={onUpdateInputChange}
              placeholder={updateForm.authorName}
              style={{
                border: "1px solid #ccc",
                borderRadius: "5px",
                width: 400,
                height: 50,
              }}
            />
          </div>

          <div style={{ display: "flex", justifyContent: "space-between" }}>
            <p>저자 이메일</p>
            <input
              type="text"
              name="authorEmail"
              value={updateForm.authorEmail}
              onChange={onUpdateInputChange}
              placeholder={updateForm.authorEmail}
              style={{
                border: "1px solid #ccc",
                borderRadius: "5px",
                width: 400,
                height: 50,
              }}
            />
          </div>
          {modalMessage && <p>{modalMessage}</p>}
          <button
            onClick={() => onUpdateAuthorClick(authorId)}
            css={style.createButton}
            style={{ margin: "10px auto", marginRight: 0, marginTop: "auto" }}
          >
            수정
          </button>
        </div>
      </div>
    </>
  );

  return (
    <>
      <div className="topBar">
        <CreateAuthor />
        <div style={{display: "flex", gap: 12}}>
          
        <input
          type="text"
          name="authorName"
          value={searchForm.authorName}
          placeholder="조회할 저자 이름을 입력하세요"
          onChange={onSearchInputChange}
          onKeyDown={handleKeyDown}
          css={style.searchInput}
          />
        <button onClick={onGetAllAuthorsByNameClick}>검색</button>
          </div>
      </div>
      <table>
        <thead>
          <tr>
            <th>저자 이름</th>
            <th>저자 이메일</th>
            <th>작업</th>
          </tr>
        </thead>
        <tbody>{authorList}</tbody>
      </table>
      {message && <p>{message}</p>}

      {modalStatus && (
        <Modal
          isOpen={modalStatus}
          onClose={() => {
            setModalStatus(false), setMessage("");
          }}
          children={modalContent}
        ></Modal>
      )}

      {/* 페이지네이션 */}
      {pagedAuthors.length > 0 && (
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
    </>
  );
}

export default ElseAuthor;
