import React from 'react'
import LocationPage from '../views/location/LocationPage';
import RequireAuth from '@/components/auth/RequireAuth';
import { Route } from 'react-router-dom';

function LocationRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/branch/locations" element={<LocationPage />}/>
      </Route>
    </>
  );
}

export default LocationRoutes