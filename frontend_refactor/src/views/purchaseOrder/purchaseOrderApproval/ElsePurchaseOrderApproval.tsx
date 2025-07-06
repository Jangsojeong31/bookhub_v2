import {
  getAllPurchaseOrderApprovalByCriteria,
  getAllPurchaseOrderApprovalByDate,
} from "@/apis/purchaseOrder/purchaseOrderApproval";
import { PurchaseOrderStatus } from "@/dtos/purchaseOrderApproval/request/purchaseOrder-approve.request.dto";
import { PurchaseOrderApprovalResponseDto } from "@/dtos/purchaseOrderApproval/response/purchaseOrderApproval.respose.dto";
import React, { useState } from "react";
import { useCookies } from "react-cookie";
import { useNavigate } from "react-router-dom";

function ElsePurchaseOrderApproval() {
  const [searchForm, setSearchForm] = useState<{
    employeeName: string;
    isApproved: boolean | null;
  }>({
    employeeName: "",
    isApproved: null,
  });

  const [dateForm, setDateForm] = useState({
    startDate: "",
    endDate: "",
  });

  const [cookies] = useCookies(["accessToken"]);
  const [message, setMessage] = useState("");
  const [purchaseOrderApprovals, setPurchaseOrderApprovals] = useState<
    PurchaseOrderApprovalResponseDto[]
  >([]);

  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 10;

  const navigate = useNavigate();

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setDateForm({ ...dateForm, [name]: value });
  };

  //* 조회 조건으로 조회 -- 조건 선택안하면 전체 조회
  const onGetPurchaseOrderByCriteria = async () => {
    setPurchaseOrderApprovals([]);
    const { employeeName, isApproved } = searchForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      navigate("/auth/login");
      return;
    }

    const response = await getAllPurchaseOrderApprovalByCriteria(
      employeeName,
      isApproved,
      token
    );
    const { code, message, data } = response;

    if (!code) {
      setMessage(message);
      return;
    }

    if (Array.isArray(data)) {
      setPurchaseOrderApprovals(data);
      setMessage("");
    } else {
      setMessage("올바른 검색 조건을 입력해주세요.");
    }
  };

  // * 날짜로 조회
  const onGetPurchaseOrderApprovalByDate = async () => {
    setPurchaseOrderApprovals([]);
    const { startDate, endDate } = dateForm;
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    const response = await getAllPurchaseOrderApprovalByDate(
      startDate,
      endDate,
      token
    );
    const { code, message, data } = response;

    if (!code) {
      setMessage(message);
      return;
    }

    if (Array.isArray(data)) {
      setPurchaseOrderApprovals(data);
      setMessage("");
    } else {
      setMessage("올바른 검색 조건을 입력해주세요.");
    }
  };

  const totalPages = Math.ceil(purchaseOrderApprovals.length / itemsPerPage);

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

  const pagedPurchaseOrderApprovals = purchaseOrderApprovals.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  // *노출 리스트
  const responsePurchaseOrderApprovalList = purchaseOrderApprovals.map(
    (purchaseOrderApproval, index) => {
      return (
        <tr key={index}>
          <td>{purchaseOrderApproval.employeeName}</td>
          <td>{purchaseOrderApproval.isApproved ? "승인" : "승인 거부"}</td>
          <td>
            {new Date(purchaseOrderApproval.approvedDateAt).toLocaleString(
              "ko-KR"
            )}
          </td>

          <td></td>
          <td>{purchaseOrderApproval.poDetail.branchName}</td>
          <td>{purchaseOrderApproval.poDetail.employeeName}</td>
          <td>{purchaseOrderApproval.poDetail.isbn}</td>
          <td>{purchaseOrderApproval.poDetail.bookTitle}</td>
          <td>{purchaseOrderApproval.poDetail.purchaseOrderAmount}</td>
          <td>
            {purchaseOrderApproval.poDetail.purchaseOrderStatus ==
            PurchaseOrderStatus.REQUESTED
              ? "요청중"
              : purchaseOrderApproval.poDetail.purchaseOrderStatus ===
                PurchaseOrderStatus.APPROVED
              ? "승인"
              : "거부"}
          </td>
        </tr>
      );
    }
  );

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between" }}>
        <div
          style={{
            flex: "2",
            display: "flex",

            gap: "12px",
          }}
        >
          <input
            type="text"
            name="employeeName"
            value={searchForm.employeeName}
            placeholder="승인담당자"
            onInput={(e: React.ChangeEvent<HTMLInputElement>) => {
              setSearchForm({ ...searchForm, employeeName: e.target.value });
            }}
            style={{ border: "1px solid #ccc", textAlign: "center" }}
          />
          <select
            name="isApproved"
            value={
              searchForm.isApproved == null ? "" : String(searchForm.isApproved)
            }
            onChange={(e) =>
              setSearchForm({
                ...searchForm,
                isApproved:
                  e.target.value == ""
                    ? null
                    : e.target.value === "true"
                    ? true
                    : false,
              })
            }
          >
            <option value="">전체 (승인여부)</option>
            <option value="true">승인</option>
            <option value="false">승인 거부</option>
          </select>
          <button
            onClick={onGetPurchaseOrderByCriteria}
            style={{ border: "1px solid #ccc" }}
          >
            검색
          </button>
        </div>

        <div
          style={{
            display: "flex",
            gap: "12px",
          }}
        >
          <input
            type="date"
            name="startDate"
            value={dateForm.startDate}
            placeholder="시작일"
            onInput={onInputChange}
            style={{ border: "1px solid #ccc", width: 150 }}
          />
          <input
            type="date"
            name="endDate"
            value={dateForm.endDate}
            placeholder="종료일"
            onInput={onInputChange}
            style={{ border: "1px solid #ccc", width: 150 }}
          />
          <button
            onClick={onGetPurchaseOrderApprovalByDate}
            style={{ border: "1px solid #ccc" }}
          >
            검색
          </button>
        </div>
      </div>

      {purchaseOrderApprovals && (
        <table
          style={{
            border: "1px solid black",
            borderCollapse: "collapse",
            width: "100%",
            fontFamily: "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif",
            fontSize: "14px",
          }}
        >
          <thead>
            <tr>
              <th>승인 담당자</th>
              <th>승인 여부</th>
              <th>승인 일자</th>

              <th>[발주서 사항]</th>
              <th>지점명</th>
              <th>발주 담당자</th>
              <th>ISBN</th>
              <th>책 제목</th>
              <th>발주 수량</th>
              <th>승인 상태</th>
            </tr>
          </thead>
          <tbody>{responsePurchaseOrderApprovalList}</tbody>
        </table>
      )}
      {message && <p>{message}</p>}
      {/* 페이지네이션 */}
      {purchaseOrderApprovals.length > 0 && (
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
    </div>
  );
}

export default ElsePurchaseOrderApproval;
