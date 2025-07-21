import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockLogListPage from '../views/stock-logs/StockLogListPage'
import StockLogsAdmin from '../views/stock-logs/StockLogsAdmin'
import RequireAuth from '@/components/auth/RequireAuth'

function StockLogRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/stock-logs/admin" element={<StockLogsAdmin />}/>
      </Route>
      
      <Route path = "/stock-logs/branch" element={<StockLogListPage/>}/>
    </>
  )
}

export default StockLogRoutes