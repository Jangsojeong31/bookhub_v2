/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import React, { useState } from "react";
import { createAuthor } from "@/apis/author/author";
import { useCookies } from "react-cookie";
import Modal from "@/apis/constants/Modal";

// 저자 등록
function CreateAuthor() {
  const [modalStatus, setModalStatus] = useState(false);
  const [form, setForm] = useState({
    authorName: "",
    authorEmail: "",
  });
  const [message, setMessage] = useState("");
  const [modalMessage, setModalMessage] = useState("");
  const [cookies] = useCookies(["accessToken"]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // 등록 모달창 열기
  const openCreateModal = () => {
    setModalStatus(true);
  };

  // 모달창 내용
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
            <h2 style={{ color: "#265185", textAlign: "center" }}>저자 등록</h2>
  
            <div style={{ display: "flex", justifyContent: "space-between" }}>
              <p>저자 이름</p>
              <input
                type="text"
                name="authorName"
                value={form.authorName}
                onChange={onInputChange}
                placeholder="저자 이름을 입력하세요."
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
                value={form.authorEmail}
                onChange={onInputChange}
                placeholder="저자 이메일을 입력하세요."
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
              onClick={() => onCreateAuthorClick()}
              css={style.createButton}
              style={{ margin: "10px auto", marginRight: 0, marginTop: "auto" }}
            >
              등록
            </button>
          </div>
        </div>
      </>
    );

  // 등록 버튼 클릭
  const onCreateAuthorClick = async () => {
    const dto = {
      authorName: form.authorName,
      authorEmail: form.authorEmail,
    };
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    try {
      const response = await createAuthor(dto, token);
      const { code, message } = response;

      if (code != "SU") {
        alert(message);
        return;
      }

      setMessage("등록이 완료되었습니다.");
      setForm({
        authorName: "",
        authorEmail: ""
      })
    } catch (error) {
      console.error(error);
      alert("등록 중 오류가 발생했습니다.");
    }
  };

  return (
    <div>
        <button onClick={openCreateModal} css={style.createButton}>
          저자 등록
        </button>
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
    </div>
  );
}
export default CreateAuthor;
