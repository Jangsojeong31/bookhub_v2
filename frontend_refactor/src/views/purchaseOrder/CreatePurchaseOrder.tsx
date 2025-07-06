/** @jsxImportSource @emotion/react */
import * as style from "@/styles/style";
import Modal from "@/apis/constants/Modal";
import { createPurchaseOrder } from "@/apis/purchaseOrder/purchaseOrder";
import { PurchaseOrderCreateRequestDto } from "@/dtos/purchaseOrder/request/purchaseOrder-create.request.dto";
import { PurchaseOrderRequestDto } from "@/dtos/purchaseOrder/request/purchaseOrder.request.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import "./CreateModal.css";

function CreatePurchaseOrder() {
  const [form, setForm] = useState({
    isbn: "",
    purchaseOrderAmount: "",
  });
  const [purchaseOrders, setRequestOrders] = useState<
    PurchaseOrderRequestDto[]
  >([]);
  const [message, setMessage] = useState("");
  const [modalStatus, setModalStatus] = useState(false);
  const [cookies] = useCookies(["accessToken"]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setMessage("");
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  // 추가 버튼 누르면
  const onAddPurchaseOrder = () => {
    const { isbn, purchaseOrderAmount } = form;

    if (!isbn || !purchaseOrderAmount) {
      setMessage("모든 항목을 입력해주세요");
      return;
    }

    const parsedAmount = parseInt(purchaseOrderAmount, 10);

    const newPurchaseOrder: PurchaseOrderRequestDto = {
      isbn,
      purchaseOrderAmount: parsedAmount,
    };
    setRequestOrders([...purchaseOrders, newPurchaseOrder]);

    setForm({ isbn: "", purchaseOrderAmount: "" });

    setMessage("");
  };

  // 노출 리스트(request)
  const reqeustPurchaseOrderList = purchaseOrders.map(
    (purchaseOrder, index) => {
      return (
        <tr key={index}>
          <td>{purchaseOrder.isbn}</td>
          <td>{purchaseOrder.purchaseOrderAmount}</td>
        </tr>
      );
    }
  );

  // 등록
  const onCreatePurchaseOrderClick = async () => {
    setModalStatus(true);

    if (purchaseOrders.length === 0) {
      setMessage("등록하실 내용을 입력 후 추가 버튼을 눌러주세요.");
      return;
    }

    const requestBody: PurchaseOrderCreateRequestDto = { purchaseOrders };
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    try {
      const response = await createPurchaseOrder(requestBody, token);
      const { code, message, data: responseOrders } = response;

      if (code != "SU") {
        setMessage(message);
        return;
      }

      if (Array.isArray(responseOrders)) {
        setMessage("");
      } else {
        setMessage("데이터 형식이 올바르지 않습니다.");
      }

      setRequestOrders([]);
      setMessage("등록이 완료되었습니다.");
    } catch (error) {
      console.error(error);
      alert("등록 중 오류가 발생했습니다.");
    }
  };

  const modalContent: React.ReactNode = (
    <>
      <h3 style={{ color: "#265185" }}>발주 요청서 작성</h3>
      <div>{message}</div>
      <div className="parent">
        <div className="center">
          <div className="table-scroll-container">
            <table
              style={{
                width: 500,
                margin: "16px 0",
                borderCollapse: "collapse",
                backgroundColor: "white",
              }}
            >
              <thead>
                <tr>
                  <th>ISBN</th>
                  <th>발주량</th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td
                    style={{
                      padding: "0",
                      height: "48px",
                    }}
                  >
                    <input
                      type="text"
                      placeholder="ISBN을 입력해주세요"
                      name="isbn"
                      value={form.isbn}
                      onChange={onInputChange}
                      style={{
                        width: "100%",
                        height: "100%",
                        boxSizing: "border-box",
                        padding: "4px 8px",
                        border: "1px solid transparent",
                      }}
                    />
                  </td>
                  <td
                    style={{
                      padding: "0",
                      height: "48px",
                    }}
                  >
                    <input
                      type="text"
                      placeholder="발주량을 입력해주세요"
                      name="purchaseOrderAmount"
                      value={form.purchaseOrderAmount}
                      onChange={onInputChange}
                      style={{
                        width: "100%",
                        height: "100%",
                        boxSizing: "border-box",
                        padding: "4px 8px",
                        border: "1px solid transparent",
                      }}
                    />
                  </td>
                  <td
                    style={{
                      border: "1px solid #ccc",
                      padding: "0",
                      height: "48px",
                      width: 30,
                    }}
                  >
                    <button
                      onClick={onAddPurchaseOrder}
                      style={{
                        width: "30",
                        height: "100%",
                        boxSizing: "border-box",
                        padding: "4px 8px",
                        border: "1px solid transparent",
                      }}
                    >
                      추가
                    </button>
                  </td>
                </tr>
                {reqeustPurchaseOrderList}
              </tbody>
            </table>
          </div>
        </div>
        <div className="right">
          <button onClick={onCreatePurchaseOrderClick} css={style.createButton}>
            등록
          </button>
        </div>
      </div>
    </>
  );

  return (
    <>
      <button onClick={() => setModalStatus(true)} css={style.createButton}>
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
