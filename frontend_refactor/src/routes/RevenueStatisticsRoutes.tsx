//views/statistics/revenue/index.tsx
import { Route, Routes } from 'react-router-dom';
import RevenueDashboard from '../views/statistics/revenue/RevenueDashboard';
import RevenueWeekday from '../views/statistics/revenue/WeekdayRevenue'
import WeeklyRevenue from '../views/statistics/revenue/WeeklyRevenue';
import MonthlyRevenue from '../views/statistics/revenue/MonthlyRevenue';
import BranchRevenue from '../views/statistics/revenue/BranchRevenue';
import RequireAuth from '@/components/auth/RequireAuth';

function RevenueStatistsicsRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/statistics/revenue/period" element={<RevenueDashboard />}/>
        <Route path="/statistics/revenue/branch" element={<BranchRevenue />}/>
      </Route>
    </>
  )
}

export default RevenueStatistsicsRoutes;