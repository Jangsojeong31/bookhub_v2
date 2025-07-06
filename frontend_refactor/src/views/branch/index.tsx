import React from "react";
import { Route } from "react-router-dom";
import BranchSearch from "./BranchSearch";
import CreateBranch from "./CreateBranch";
import RequireAuth from "@/components/auth/RequireAuth";

function Branch() {
  return (
    <>
      <Route
        path="/branches"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <BranchSearch />
          </RequireAuth>
        }
      />
      <Route
        path="/branches/manage"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <CreateBranch />
          </RequireAuth>
        }
      />
    </>
  );
}

export default Branch;
