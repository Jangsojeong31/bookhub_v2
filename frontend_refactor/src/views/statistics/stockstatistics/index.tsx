import React from 'react'
import TimeStockStatistics from './TimeStockStatistics'
import CategoryStockStatistics from './CategoryStockStatistics'
import BranchStockStatistics from './BranchStockStatistics'
import ZeroStockStatistics from './ZeroStockStatistics'
import { Route, Routes } from 'react-router-dom'

function StockStatistics() {
  return (
    <Routes>
      <Route path="time" element={<TimeStockStatistics/>}/>
      <Route path="category" element={<CategoryStockStatistics/>}/>
      <Route path="branch" element={<BranchStockStatistics/>}/>
      <Route path="zero" element={<ZeroStockStatistics/>}/>
    </Routes>
  )
  
}

export default StockStatistics