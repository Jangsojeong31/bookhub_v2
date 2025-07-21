import React from "react";
import { Route } from "react-router-dom";
import RequireAuth from "@/components/auth/RequireAuth";
import ElseAuthor from "@/views/author/ElseAuthor";

function AuthorRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/authors" element={<ElseAuthor />}/>
      </Route>
    </>
  );
}

export default AuthorRoutes;
