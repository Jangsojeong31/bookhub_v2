import React from 'react'
import { Route, Routes } from 'react-router-dom'
import RequireAuth from '@/components/auth/RequireAuth'
import StockPageAdmin from '@/views/stocks/StockPageAdmin'
import StockPageManager from '@/views/stocks/StockPageManager'

function StockRoutes() {
  return (
    <>
      <Route path = "/stocks/manager" element = {<StockPageManager/>}/>
      
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/stocks/admin" element={<StockPageAdmin />}/>
      </Route>
    </>
  )
}

export default StockRoutes

