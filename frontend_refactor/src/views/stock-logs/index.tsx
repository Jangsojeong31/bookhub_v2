import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockLogListPage from './StockLogListPage'
import StockLogsAdmin from './StockLogsAdmin'


function StockLog() {
  return (
    <Routes>
      <Route path = "/admin" element={<StockLogsAdmin/>}/>
      <Route path = "/branch" element={<StockLogListPage/>}/>
    </Routes>
  )
}

export default StockLog