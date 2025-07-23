import React from "react";
import { Route } from "react-router-dom";
import RequireAuth from "@/components/auth/RequireAuth";
import AuthorPage from "@/views/author/AuthorPage";

function AuthorRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/authors" element={<AuthorPage />}/>
      </Route>
    </>
  );
}

export default AuthorRoutes;
