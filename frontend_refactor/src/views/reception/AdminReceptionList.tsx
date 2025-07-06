import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { ReceptionListResponseDto } from "@/dtos/reception/response/receptionlist-response.dto";
import { getAdminReceptionApproval } from "@/apis/reception/reception";
import { GET_BRANCH_URL } from "@/apis/constants/khj.constants";

type Branch = {
  branchId: number;
  branchName: string;
};

function AdminReceptionList() {
  const [cookies] = useCookies(["accessToken"]);
  const [branches, setBranches] = useState<Branch[]>([]);
  const [branchName, setBranchName] = useState("");
  const [bookIsbn, setBookIsbn] = useState("");
  const [logs, setLogs] = useState<ReceptionListResponseDto[]>([]);

  useEffect(() => {
  fetch(`${GET_BRANCH_URL}?branchLocation`)
    .then((res) => {
      console.log("응답 상태:", res.status);
      return res.json();
    })
    .then((data) => {
      console.log("실제 응답 데이터:", data);

      if (!data || !data.data || data.data.length === 0) {
        console.warn("지점이 없습니다.");
      } else {
        setBranches(data.data);
      }
    })
    .catch((e) => console.error("지점 조회 실패:", e));
}, []);



  // 수령 확인 로그 조회
  const fetchLogs = async () => {
    const token = cookies.accessToken;
    if (!token) return alert("토큰 없음");

    const isbnParam = bookIsbn.trim() === "" ? undefined : bookIsbn;
    const branchParam = branchName.trim() === "" ? undefined : branchName;

    try {
      const res = await getAdminReceptionApproval(token, branchParam, isbnParam);
      if (res.code === "SU") {
        setLogs(res.data ?? []);
      } else {
        alert(res.message);
      }
    } catch (err) {
      console.error(err);
      alert("조회 실패");
    }
  };

  // 전체 초기화 및 재조회
  const fetchAllLogs = () => {
    setBranchName("");
    setBookIsbn("");
    fetchLogs();
  };

  return (
    <div>
      <h2>관리자 수령 확인 로그 조회</h2>
      <div style={{ display: "flex", gap: "10px", marginBottom: "16px" }}>
        <select value={branchName} onChange={(e) => setBranchName(e.target.value)}>
          <option value="">전체 지점</option>
          {branches.map((branch) => (
            <option key={branch.branchId} value={branch.branchName}>
              {branch.branchName}
            </option>
          ))}
        </select>

        <input
          type="text"
          placeholder="ISBN 검색"
          value={bookIsbn}
          onChange={(e) => setBookIsbn(e.target.value)}
        />

        <button onClick={fetchLogs}>조회</button>
        <button onClick={fetchAllLogs}>전체</button>
      </div>

      {logs.length === 0 ? (
        <p>조회된 로그가 없습니다.</p>
      ) : (
        <table>
          <thead>
            <tr>
              <th>지점</th>
              <th>도서 제목</th>
              <th>ISBN</th>
              <th>수량</th>
              <th>수령일</th>
              <th>수령자</th>
            </tr>
          </thead>
          <tbody>
            {logs.map((item, index) => (
              <tr key={item.bookReceptionApprovalId ?? index}>
                <td>{item.branchName}</td>
                <td>{item.bookTitle}</td>
                <td>{item.bookIsbn}</td>
                <td>{item.purchaseOrderAmount}</td>
                <td>{new Date(item.receptionDateAt).toISOString().slice(0, 10)}</td>
                <td>{item.receptionEmployeeName}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default AdminReceptionList;
