import React from 'react'
import RevenueWeekday from './WeekdayRevenue'
import BranchRevenue from './BranchRevenue';
import WeeklyRevenue from './WeeklyRevenue';
import MonthlyRevenue from './MonthlyRevenue';

function RevenueDashboard() {
  return (
    
    <div style={{
        display: "grid",
        gridTemplateColumns: "1fr 1fr",
        gridTemplateRows: "1fr 1fr",
        gap: "12px",
        width: "200vh",
        height: "50vh",
        padding: "12px",
      }}>
<div><RevenueWeekday/></div>
<div><BranchRevenue/></div>
<div><WeeklyRevenue/></div>
<div><MonthlyRevenue/></div>

      </div>
  )
}

export default RevenueDashboard;