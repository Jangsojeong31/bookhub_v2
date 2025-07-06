//views/statistics/revenue/index.tsx
import { Route, Routes } from 'react-router-dom';
import RevenueDashboard from './RevenueDashboard';
import RevenueWeekday from './WeekdayRevenue'
import WeeklyRevenue from './WeeklyRevenue';
import MonthlyRevenue from './MonthlyRevenue';
import BranchRevenue from './BranchRevenue';

function Revenue() {
  return (
    <Routes>
      <Route index element = {<RevenueDashboard/>}/>
      <Route path="weekday" element={<RevenueWeekday/>}/>
      <Route path="weekly" element={<WeeklyRevenue/>}/>
      <Route path="monthly" element={<MonthlyRevenue/>}/>
      <Route path="branch" element={<BranchRevenue/>}/>
    </Routes>
  )
}

export default Revenue;