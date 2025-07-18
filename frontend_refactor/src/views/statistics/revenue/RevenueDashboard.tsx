import WeeklyRevenue from './WeeklyRevenue';
import MonthlyRevenue from './MonthlyRevenue';
import { NavLink } from 'react-router-dom';
import WeekdayRevenue from './WeekdayRevenue';

function RevenueDashboard() {
  return (
    <>
    <h2>매출 통계</h2>
    <div style={{ marginBottom: 16, display: "flex", gap: 12 }}>
        {[
          { to: "/statistics/revenue/period", label: "기간별" },
          { to: "/statistics/revenue/branch", label: "지점별" },
        ].map(({ to, label }) => (
          <NavLink
            key={to}
            to={to}
            style={({ isActive }) => ({
              backgroundColor: isActive ? "#265185" : "#f0f0f0",
              color: isActive ? "white" : "#333",
              padding: "10px 20px",
              borderRadius: 6,
              textDecoration: "none",
              fontWeight: isActive ? "bold" : "normal",
              transition: "background-color 0.3s",
            })}
          >
            {label}
          </NavLink>
        ))}
      </div>

<div style={{ marginTop: 0,}}>
        <MonthlyRevenue />
      </div>
      <div
        style={{
          display: "flex",
          gap: 15,
          marginBottom: 64,
        }}
      >
        <div style={{ flex: 1 }}>
          <WeekdayRevenue />
        </div>
        <div style={{ flex: 1 }}>
          <WeeklyRevenue />
        </div>
      </div>

      
    </>
    
    
  )
}

export default RevenueDashboard;