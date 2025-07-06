import React, { useEffect, useState } from 'react'
import { useParams, useSearchParams } from 'react-router-dom'
import { useCookies } from 'react-cookie'
import { StockLogResponseDto } from '@/dtos/stock/StockLog.response.dto'
import { getStockLogsByDate } from '@/apis/stock/stockLog'
import './StockLogPage.css'

const StockLogByDatePage = () => {
  const { branchId } = useParams()
  const [searchParams] = useSearchParams()
  const [cookies] = useCookies(['accessToken'])
  const [logs, setLogs] = useState<StockLogResponseDto[]>([])

  const start = searchParams.get('start')
  const end = searchParams.get('end')

  useEffect(() => {
    if (!branchId || !start || !end) return
    getStockLogsByDate(Number(branchId), start, end, cookies.accessToken)
      .then(res => setLogs(res.data))
      .catch(() => setLogs([]))
  }, [branchId, start, end, cookies.accessToken])

  return (
    <div className="stock-log-wrapper">
      <h2 className="stock-log-title">
        날짜별 재고 로그 (branchId: {branchId}, {start} ~ {end})
      </h2>
      <table className="stock-log-table">
        <thead>
          <tr>
            <th>날짜</th>
            <th>유형</th>
            <th>도서명</th>
            <th>변경수량</th>
            <th>최종수량</th>
            <th>비고</th>
          </tr>
        </thead>
        <tbody>
          {logs.length === 0 ? (
            <tr>
              <td colSpan={6} className="no-data">로그가 없습니다.</td>
            </tr>
          ) : (
            logs.map((log) => (
              <tr key={log.stockLogId}>
                <td>{log.actionDate}</td>
                <td>{log.type}</td>
                <td>{log.bookTitle}</td>
                <td>{log.amount}</td>
                <td>{log.bookAmount}</td>
                <td>{log.description}</td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  )
}

export default StockLogByDatePage
