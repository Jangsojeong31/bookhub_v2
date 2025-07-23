import React from "react";
import CreatePublisher from "../views/publisher/CreatePublisher";
import { Route, Routes } from "react-router-dom";
import UpdatePublisher from "../views/publisher/UpdatePublisher";
import PublisherPage from "../views/publisher/PublisherPage";
import RequireAuth from "@/components/auth/RequireAuth";

function PublisherRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/publishers" element={<PublisherPage />}/>
      </Route>
    </>
  );
}

export default PublisherRoutes;
