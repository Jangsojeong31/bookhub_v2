import React from 'react'
import { Route, Routes } from 'react-router-dom'
import StockPage from './StockPage'

function Stock() {
  return (
    <Routes>
      <Route path = "/" element = {<StockPage/>}/>

    </Routes>
  )
}

export default Stock

