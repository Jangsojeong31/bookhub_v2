import React from "react";
import CreatePublisher from "./CreatePublisher";
import { Route, Routes } from "react-router-dom";
import UpdatePublisher from "./UpdatePublisher";
import PublisherPage from "./PublisherPage";
import RequireAuth from "@/components/auth/RequireAuth";

function Publisher() {
  return (
    <Routes>
      {/* <Route path='/publishers' element={<CreatePublisherModalLauncher />} /> */}
      <Route
        path="/"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <PublisherPage />
          </RequireAuth>
        }
      />
      {/* <Route path='/:publisherId' element={<UpdatePublisher />} /> */}
    </Routes>
  );
}

export default Publisher;
