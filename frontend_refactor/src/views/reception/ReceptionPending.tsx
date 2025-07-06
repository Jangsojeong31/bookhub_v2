import { getAllPendingReception, putReception } from '@/apis/reception/reception';
import { ReceptionListResponseDto } from '@/dtos/reception/response/receptionlist-response.dto';
import React, { useEffect, useState } from 'react'
import { useCookies } from 'react-cookie'

function ReceptionPending() {
  const [ cookies ] = useCookies(["accessToken"]);
  const [ pendingList, setPendingList ] = useState<ReceptionListResponseDto[]>([]);
  const [ message, setMessage ] = useState("");
  const [ currentPage, setCurrentPage ] = useState(0);
  const [ isReceptionApproved, setIsReceptionApproved ] = useState(false);
  const itemsPerPage = 10;

  useEffect(() => {
    const fetchData = async () => {
      const token = cookies.accessToken;
      if (!token) {
        alert("인증 토큰이 없습니다.");
        return;
      }
      try {
        const res = await getAllPendingReception(token);
        if (res.code !== "SU") {
          setMessage(res.message);
          return;
        }
        setPendingList(res.data ?? []);
      } catch (error){
        console.error(error);
        setMessage("데이터를 불러오는 데 실패했습니다");
      }
    };

    fetchData();
  }, [cookies]);

  const totalPages = Math.ceil(pendingList.length / itemsPerPage);

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

  const receptionsToDisplay = pendingList.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  const onReceptionApprove = async(bookReceptionApprovalId: number) => {
    const token = cookies.accessToken;

    if(!token) {
      alert('인증 토큰이 없습니다.');
      return;
    }

    const response = await putReception(token, bookReceptionApprovalId)
    const { code, message } = response;

    if(!code) {
      setMessage(message);
      return;
    }

    alert("수령 확인 성공");
    setPendingList(prev => prev.filter(item => item.bookReceptionApprovalId !== bookReceptionApprovalId));
  }



  return (
    <div>
      <h2>수령 확인 대기 리스트</h2>
        <div className='table-container'>
          <table>
            <thead>
              <tr>
                <th></th>
                <th>지점</th>
                <th>도서 제목</th>
                <th>ISBN</th>
                <th>수량</th>
                <th></th>
              </tr>
            </thead>
            <tbody>
              {receptionsToDisplay.map((item, index) =>(
                <tr key={item.bookReceptionApprovalId}>
                  <td>{currentPage * itemsPerPage +index + 1}</td>
                  <td>{item.branchName}</td>
                  <td>{item.bookTitle}</td>
                  <td>{item.bookIsbn}</td>
                  <td>{item.purchaseOrderAmount}</td>
                  <td><button className='Button' onClick={() => onReceptionApprove(item.bookReceptionApprovalId)}>수령</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {/* 페이지네이션 */}
      {pendingList.length > 0 && (
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
  )
}

export default ReceptionPending