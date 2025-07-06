import { StockActionType } from "@/apis/enums/StockActionType"
import { getStockLogsByType } from "@/apis/stock/stockLog"
import { StockLogResponseDto } from "@/dtos/stock/StockLog.response.dto"
import { useState, useEffect } from "react"
import { useCookies } from "react-cookie"
import { useParams, useSearchParams, useNavigate } from "react-router-dom"

const StockLogByTypePage = () => {
  const { branchId } = useParams()
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const [cookies] = useCookies(['accessToken'])
  const [logs, setLogs] = useState<StockLogResponseDto[]>([])
  const type = searchParams.get('type') || StockActionType.IN

  useEffect(() => {
    if (!branchId) return
    getStockLogsByType(Number(branchId), type, cookies.accessToken)
      .then(res => setLogs(res.data))
      .catch(() => setLogs([]))
  }, [branchId, type, cookies.accessToken])

  const handleTypeChange = (newType: StockActionType) => {
    navigate(`/stock-logs/branch/${branchId}/type?type=${newType}`)
  }

  return (
    <div>
      <h2>유형별 재고 로그</h2>

      {/* 타입 선택 토글 */}
      <div >
        {Object.values(StockActionType).map((t) => (
          <button className="stock-button"
            key={t}
            onClick={() => handleTypeChange(t)}
           
          >
            {t === StockActionType.IN && '입고'}
            {t === StockActionType.OUT && '출고'}
            {t === StockActionType.LOSS && '분실/파손'}
          </button>
        ))}
      </div>

      {/* 로그 테이블 */}
      <table >
        <thead>
          <tr >
            <th >날짜</th>
            <th >유형</th>
            <th >도서명</th>
            <th >수량</th>
          </tr>
        </thead>
        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan={4} >
                해당 로그가 없습니다.
              </td>
            </tr>
          ) : (
            logs.map(log => (
              <tr key={log.stockLogId}>
                <td >{log.actionDate}</td>
                <td >{log.type}</td>
                <td >{log.bookTitle}</td>
                <td >{log.amount}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}

export default StockLogByTypePage