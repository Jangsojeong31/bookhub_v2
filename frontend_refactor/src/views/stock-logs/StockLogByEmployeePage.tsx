import React, { useEffect, useState } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useCookies } from 'react-cookie'
import { StockLogResponseDto } from '@/dtos/stock/StockLog.response.dto'
import { getStockLogsByEmployee } from '@/apis/stock/stockLog'
import './StockLogPage.css' // 여기에서 테이블 클래스도 같이 적용됨을 전제
import { useEmployeeStore } from '@/stores/employee.store'

const StockLogByEmployeePage = () => {
  const [cookies] = useCookies(['accessToken'])
  const [logs, setLogs] = useState<StockLogResponseDto[]>([])
  const navigate = useNavigate();
    const employee = useEmployeeStore((state) => state.employee)
    
      const branchId = employee?.branchId ?? null
      const employeeId = employee?.employeeId ?? null

  useEffect(() => {
    if (!employeeId) return
    getStockLogsByEmployee(Number(employeeId), cookies.accessToken)
      .then(res => setLogs(res.data))
      .catch(() => setLogs([]))
  }, [employeeId, cookies.accessToken])

  return (
    <div className="stock-log-wrapper">
      <div style={{marginTop: 10, marginBottom: 20}}>

      <div className="button-group">
        <button className="stock-button" onClick={() => navigate(`/stock-logs/branch`)}>지점 재고로그 조회</button>
        <button className="stock-button" onClick={() => navigate(`/stock-logs/employee/${employeeId}`)}>나의 재고로그 조회</button>
      </div>
      </div>
      <h2 className="stock-log-title">나의 재고 로그 (employeeId: {employeeId})</h2>

      <table className="stock-log-table">
        <thead>
          <tr>
            <th>날짜</th>
            <th>유형</th>
            <th>도서명</th>
            <th>수량</th>
          </tr>
        </thead>
        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan={4} className="no-data">로그가 없습니다.</td>
            </tr>
          ) : (
            logs.map((log) => (
              <tr key={log.stockLogId}>
                <td>{log.actionDate}</td>
                <td>{log.type}</td>
                <td>{log.bookTitle}</td>
                <td>{log.amount}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}

export default StockLogByEmployeePage


