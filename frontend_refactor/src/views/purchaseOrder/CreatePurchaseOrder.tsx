/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import Modal from "@/components/Modal";
import { createPurchaseOrder } from "@/apis/purchaseOrder/purchaseOrder";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import "./CreateModal.css";

function CreatePurchaseOrder() {
  const [form, setForm] = useState({
    isbn: "",
    purchaseOrderAmount: "",
  });
  const [message, setMessage] = useState("");
  const [modalStatus, setModalStatus] = useState(false);
  const [cookies] = useCookies(["accessToken"]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMessage("");
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // 등록 모달창 열기
  const openCreateModal = () => {
    setModalStatus(true);
  };

  // 등록
  const onCreatePurchaseOrderClick = async () => {
    const dto = {
      isbn: form.isbn,
      purchaseOrderAmount: Number.parseInt(form.purchaseOrderAmount),
    };
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    try {
      const response = await createPurchaseOrder(dto, token);
      const { code, message, data: responseOrders } = response;

      if (code != "SU") {
        alert(message);
        return;
      }

      if (Array.isArray(responseOrders)) {
        setMessage("");
      } else {
        setMessage("데이터 형식이 올바르지 않습니다.");
      }
      alert("등록이 완료되었습니다.");
      setForm({
        isbn: "",
        purchaseOrderAmount: "",
      })
    } catch (error) {
      console.error(error);
      alert("등록 중 오류가 발생했습니다.");
    }
  };

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
                  <h2 style={{ color: "#265185", textAlign: "center" }}>발주 요청서 작성</h2>
        
                  <div style={{ display: "flex", justifyContent: "space-between" }}>
                    <p>ISBN</p>
                    <input
                      type="text"
                      name="isbn"
                      value={form.isbn}
                      onChange={onInputChange}
                      placeholder="ISBN을 입력하세요."
                      style={{
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        width: 400,
                        height: 50,
                      }}
                    />
                  </div>
        
                  <div style={{ display: "flex", justifyContent: "space-between" }}>
                    <p>발주량</p>
                    <input
                      type="text"
                      name="purchaseOrderAmount"
                      value={form.purchaseOrderAmount}
                      onChange={onInputChange}
                      placeholder="발주량을 입력하세요."
                      style={{
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        width: 400,
                        height: 50,
                      }}
                    />
                  </div>
                  <button
                    onClick={onCreatePurchaseOrderClick}
                    css={style.createButton}
                    style={{ margin: "10px auto", marginRight: 0, marginTop: "auto" }}
                  >
                    등록
                  </button>
                </div>
              </div>
    </>
  );

  return (
    <>
      <button onClick={openCreateModal} css={style.createButton}>
        발주 요청서 작성
      </button>

      {modalStatus && (
        <Modal
          isOpen={modalStatus}
          onClose={() => {
            setModalStatus(false);
            setMessage("");
          }}
          children={modalContent}
        />
      )}
    </>
  );
}

export default CreatePurchaseOrder;
