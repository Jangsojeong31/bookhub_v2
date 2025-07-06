import React from "react";
import { Route, Routes } from "react-router-dom";

import PolicySearch from "./PolicySearch";
import PolicyPage from "./PolicyPage";
import RequireAuth from "@/components/auth/RequireAuth";

function Policy() {
  return (
    <Routes>
      <Route path="/" element={<PolicySearch />} />
      <Route
        path="/admin"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <PolicyPage />
          </RequireAuth>
        }
      />
    </Routes>
  );
}

export default Policy;
