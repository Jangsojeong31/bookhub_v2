import React from "react";
import { Route, Routes } from "react-router-dom";

import PolicySearch from "../views/policy/PolicySearch";
import PolicyPage from "../views/policy/PolicyPage";
import RequireAuth from "@/components/auth/RequireAuth";

function PolicyRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/policies/admin" element={<PolicyPage />}/>
      </Route>

      <Route path="/policies" element={<PolicySearch />} />
    </>
  );
}

export default PolicyRoutes;
