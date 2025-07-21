import React from "react";
import { Route } from "react-router-dom";
import CreateBranch from "../views/branch/CreateBranch";
import RequireAuth from "@/components/auth/RequireAuth";

function BranchRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/branches" element={<CreateBranch />}/>
      </Route>
    </>
  );
}

export default BranchRoutes;
