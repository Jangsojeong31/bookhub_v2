import React from 'react'
import TimeStockStatistics from '../views/statistics/stockstatistics/TimeStockStatistics'
import CategoryStockStatistics from '../views/statistics/stockstatistics/CategoryStockStatistics'
import BranchStockStatistics from '../views/statistics/stockstatistics/BranchStockStatistics'
import ZeroStockStatistics from '../views/statistics/stockstatistics/ZeroStockStatistics'
import { Route, Routes } from 'react-router-dom'
import RequireAuth from '@/components/auth/RequireAuth'

function StockStatisticsRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/statistics/stocks/time" element={<TimeStockStatistics />}/>
        <Route path="/statistics/stocks/category" element={<CategoryStockStatistics />}/>
        <Route path="/statistics/stocks/branch" element={<BranchStockStatistics />}/>
        <Route path="/statistics/stocks/zero" element={<ZeroStockStatistics />}/>
      </Route>
    </>
  )
  
}

export default StockStatisticsRoutes