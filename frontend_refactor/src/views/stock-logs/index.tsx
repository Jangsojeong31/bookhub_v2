import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockLogPage from './StockLogPage'
import StockLogListPage from './StockLogListPage'
import StockLogByTypePage from './StockLogByTypePage'
import StockLogByDatePage from './StockLogByDatePage'
import StockLogByEmployeePage from './StockLogByEmployeePage'
import StockLogByBookPage from './StockLogByBookPage'

function StockLog() {
  return (
    <Routes>
      <Route path = "/" element={<StockLogPage/>}/>
      <Route path = "/branch/:branchId" element={<StockLogListPage/>}/>
      <Route path = "/branch/:branchId/type" element={<StockLogByTypePage/>}/>
      <Route path = "/employee/:employeeId" element={<StockLogByEmployeePage/>}/>
      <Route path = "/branch/:branchId/book/:bookIsbn" element={<StockLogByBookPage/>}/>
      <Route path = "/branch/:branchId/date" element={<StockLogByDatePage/>}/>
    </Routes>
  )
}

export default StockLog