import React, { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { useCookies } from 'react-cookie'
import { StockLogResponseDto } from '@/dtos/stock/StockLog.response.dto'
import { getStockLogsByBook } from '@/apis/stock/stockLog'
import './StockLogPage.css'

const StockLogByBookPage = () => {
  const { branchId, bookIsbn } = useParams()
  const [cookies] = useCookies(['accessToken'])
  const [logs, setLogs] = useState<StockLogResponseDto[]>([])

  useEffect(() => {
    if (!branchId || !bookIsbn) return
    getStockLogsByBook(Number(branchId), bookIsbn, cookies.accessToken)
      .then(res => setLogs(res.data))
      .catch(() => setLogs([]))
  }, [branchId, bookIsbn, cookies.accessToken])

  return (
    <div className="stock-log-wrapper">
      <h2 className="stock-log-title">
        도서별 재고 로그 (branchId: {branchId}, ISBN: {bookIsbn})
      </h2>

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
            logs.map(log => (
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

export default StockLogByBookPage


