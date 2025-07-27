/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import "./book.css";
import { getBookByIsbn, updateBook, hideBook } from "@/apis/book/book";
import { BookUpdateRequestDto } from "@/dtos/book/request/book-update.request.dto";

function UpdateBook() {
  const [isbnInput, setIsbnInput] = useState("");
  const [cookies] = useCookies(["accessToken"]);

  const [isbn, setIsbn] = useState("");
  const [bookPrice, setBookPrice] = useState<number>();
  const [description, setDescription] = useState("");
  const [policyId, setPolicyId] = useState<number | null>(null);
  const [categoryId, setCategoryId] = useState<number | null>(null);
  const [coverUrl, setCoverUrl] = useState<string | null>(null);
  const [coverFile, setCoverFile] = useState<File | null>(null);
  const [bookStatus, setBookStatus] = useState<
    "ACTIVE" | "INACTIVE" | "HIDDEN"
  >("ACTIVE");

  const [isBookLoaded, setIsBookLoaded] = useState(false);

  const handleSearch = async () => {
    const res = await getBookByIsbn(isbnInput, cookies.accessToken);
    if (res.code === "SU" && res.data) {
      const book = res.data;
      setIsbn(book.isbn);
      setCoverUrl(book.coverUrl!);
      setDescription(book.description ?? "");
      setBookPrice(book.bookPrice ?? null);
      setPolicyId(book.policyId ?? null);
      setCategoryId(book.categoryId ?? null);
      setBookStatus(book.bookStatus ?? "ACTIVE");
      setIsBookLoaded(true);
    } else {
      alert("도서를 찾을 수 없습니다.");
      setIsBookLoaded(false);
    }
  };

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = cookies.accessToken;
    if (!token || !isbnInput) return;

    const dto: BookUpdateRequestDto = {
      isbn: isbnInput,
      bookPrice,
      description,
      bookStatus,
      ...(policyId !== null ? { policyId } : {}),
      categoryId,
    };

    try {
      const res = await updateBook(isbnInput, dto, token, coverFile);
      if (res.code !== "SU") throw new Error(res.message);
      alert("수정 성공");
    } catch (err) {
      console.error(err);
      alert("수정 실패");
    }
  };

  const handleHide = async () => {
    const token = cookies.accessToken;
    if (!token || !isbnInput) return;

    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      const res = await hideBook(isbnInput, token);
      if (res.code !== "SU") throw new Error(res.message);
      alert("삭제(HIDDEN) 처리됨");
    } catch (err) {
      console.error(err);
      alert("삭제 실패");
    }
  };

  return (
    <div>
      <div className="filter-bar">
        <h2>ISBN으로 책 검색</h2>
          <input
          className="searchInput"
            value={isbnInput}
            onChange={(e) => setIsbnInput(e.target.value)}
            placeholder="ISBN 입력을 입력하세요"
          />
          <button type="button" onClick={handleSearch}>
            검색
          </button>
      </div>

      <div className="table-container">
        {isBookLoaded && (
          <form onSubmit={handleUpdate}>
            <table>
              <thead>
                <tr>
                  <th>ISBN</th>
                  <th>설명</th>
                  <th>가격</th>
                  <th>정책ID</th>
                  <th>카테고리ID</th>
                  <th>판매 상태</th>
                  <th>표지 이미지</th>
                  <th>수정</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{isbn}</td>
                  <td>
                    <textarea
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                      placeholder="설명"
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      value={bookPrice ?? ""}
                      onChange={(e) => setBookPrice(Number(e.target.value))}
                      placeholder="가격"
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      value={policyId ?? ""}
                      onChange={(e) => setPolicyId(Number(e.target.value))}
                      placeholder="정책ID (선택)"
                    />
                  </td>
                  <td>
                    <input
                      type="number"
                      value={categoryId ?? ""}
                      onChange={(e) => {
                        const val = e.target.value;
                        setCategoryId(val === "" ? null : Number(val));
                      }}
                      placeholder="카테고리ID"
                    />
                  </td>
                  <td>
                    <select
                      value={bookStatus}
                      className="select"
                      onChange={(e) =>
                        setBookStatus(e.target.value as "ACTIVE" | "INACTIVE")
                      }
                    >
                      <option value="ACTIVE">활성</option>
                      <option value="INACTIVE">비활성</option>
                    </select>
                  </td>
                  <td>
                    <div className="file-upload-wrapper">
                      <label
                        htmlFor="coverUpload"
                        className="file-upload-label"
                        style={{ backgroundColor: "#e74c3c" }}
                      >
                        책 표지 선택
                      </label>
                      <input
                        id="coverUpload"
                        type="file"
                        onChange={(e) =>
                          setCoverFile(e.target.files?.[0] ?? null)
                        }
                        className="file-upload-input"
                      />
                      {coverUrl ? (
                        <img
                          src={`http://localhost:8080${encodeURI(coverUrl)}`}
                          alt="cover"
                          width={90}
                          height={120}
                        />
                      ) : (
                        <span>"없음"</span>
                      )}
                    </div>
                  </td>
                  <td>
                    <button type="submit" className="modifyBtn">
                      수정하기
                    </button>
                    <button
                      type="button"
                      onClick={handleHide}
                      className="deleteBtn"
                    >
                      삭제(HIDDEN)
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </form>
        )}
      </div>
    </div>
  );
}

export default UpdateBook;
