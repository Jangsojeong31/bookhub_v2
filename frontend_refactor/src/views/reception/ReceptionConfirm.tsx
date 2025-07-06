import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { ReceptionListResponseDto } from "@/dtos/reception/response/receptionlist-response.dto";
import { getAllReceptionApproval } from "@/apis/reception/reception";
import "./Reception.css";

function ReceptionConfirm() {
  const [cookies] = useCookies(["accessToken"]);
  const [reception, setReception] = useState<ReceptionListResponseDto[]>([]);
  const [filteredReceptions, setFilteredReceptions] = useState<ReceptionListResponseDto[]>([]);
  const [dateForm, setDateForm] = useState({ startDate: "", endDate: "" });
  const [message, setMessage] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 10;



  useEffect(() => {
    const fetchData = async () => {
      const token = cookies.accessToken;
      if (!token) {
        alert("인증 토큰이 없습니다.");
        return;
      }
      try{
        const res = await getAllReceptionApproval(token);
        if (res.code !== "SU"){
          setMessage(res.message);
          return; 
        }
        setReception(res.data ?? []);
      } catch(error){
        console.error(error);
        setMessage("데이터를 불러오는 데 실패했습니다");
      }  
    };

    fetchData();
  }, [cookies]);

  const totalPages = Math.ceil(reception.length / itemsPerPage);

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

  const receptionsToDisplay = filteredReceptions.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  const handleFilter = () => {
    const { startDate, endDate } = dateForm;
    if (!startDate || !endDate){
      alert("시작일과 종료일 모두 선택해주세요");
      return;
    }

    const start = new Date(startDate);
    const end = new Date(endDate);

    const filtered = reception.filter((item) => {
      const date = new Date(item.receptionDateAt);
      return date >= start && date <= end;
    });

    setFilteredReceptions(filtered);

  }


  return (
    <div className="table-container">
      <h2>수령 확인 조회</h2>
      <div className="topBar">
        <div style={{ display: "flex", gap: "10px", marginBottom: "16px" }}>
          <input
            type="date"
            name="startDate"
            value={dateForm.startDate}
            onChange={(e) =>
              setDateForm({ ...dateForm, startDate: e.target.value })
            }
          />
          <input
            type="date"
            name="endDate"
            value={dateForm.endDate}
            onChange={(e) =>
              setDateForm({ ...dateForm, endDate: e.target.value })
            }
          />
          <button className='button' onClick={handleFilter}>조회</button>
          <button className='button' onClick={() => {
            setFilteredReceptions(reception);
            setCurrentPage(0);
          }}>전체 조회</button>
        </div>
      </div>
      <div className="table-container">
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
        {receptionsToDisplay.map((item) => (
          <tr key={item.bookReceptionApprovalId}>
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
    </div>
  {/* 페이지네이션 */}
      {reception.length > 0 && (
        <div className="footer">
          <button className="pageBtn" onClick={goPrev} disabled={currentPage === 0}>
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

export default ReceptionConfirm;
