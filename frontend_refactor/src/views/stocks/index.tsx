import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockPageManager from './StockPageManager'
import StockPageAdmin from './StockPageAdmin'

function Stock() {
  return (
    <Routes>
      <Route path = "/manager" element = {<StockPageManager/>}/>
      <Route path = "/admin" element = {<StockPageAdmin/>}/>

    </Routes>
  )
}

export default Stock

