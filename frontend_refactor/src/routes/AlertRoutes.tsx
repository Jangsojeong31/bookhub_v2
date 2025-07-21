import React from 'react'
import { Route } from 'react-router-dom'
import AlertPage from '../views/alert/AlertPage'

function AlertRoutes() {
  return (
    <Route path="/alerts" element={<AlertPage />} />
  )
}

export default AlertRoutes