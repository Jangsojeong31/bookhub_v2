import React from 'react'
import { Outlet } from 'react-router-dom'
import Header from './header/Header'
import Sidebar from './sidebar/Sidebar'

function MainLayouts() {
  return (
    <div
        style={{
          height: "100vh",
          display: "flex",
          flexDirection: "column",
          border: "none",
        }}
      >
        <Header />
        <div style={{ flex: 1, display: "flex", minHeight: 0 }}>
          <Sidebar />
          <main
            style={{
              flex: 1,
              padding: "30px",
              minWidth: "1500px",
            }}
          >
            <Outlet />
          </main>
        </div>
      </div>
  )
}

export default MainLayouts