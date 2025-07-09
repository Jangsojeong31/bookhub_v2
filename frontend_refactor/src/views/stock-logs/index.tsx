import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockLogPage from './StockLogPage'
import StockLogListPage from './StockLogListPage'
import StockLogByEmployeePage from './StockLogByEmployeePage'


function StockLog() {
  return (
    <Routes>
      {/* <Route path = "/" element={<StockLogPage/>}/> */}
      <Route path = "/branch" element={<StockLogListPage/>}/>
      <Route path = "/employee/:employeeId" element={<StockLogByEmployeePage/>}/>
    </Routes>
  )
}

export default StockLog