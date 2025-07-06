import { Route } from "react-router-dom";
import ReceptionConfirm from "./ReceptionConfirm";
import ReceptionPending from "./ReceptionPending";
import AdminReceptionList from "./AdminReceptionList";
import RequireAuth from "@/components/auth/RequireAuth";

function Reception() {
  return (
    <>
      <Route
        path="/reception/confirmed"
        element={
          <RequireAuth allowedRoles={["ADMIN", "MANAGER"]}>
            <ReceptionConfirm />
          </RequireAuth>
        }
      />
      <Route
        path="/reception/pending"
        element={
          <RequireAuth allowedRoles={["ADMIN", "MANAGER"]}>
            <ReceptionPending />
          </RequireAuth>
        }
      />
      <Route
        path="/reception/logs"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <AdminReceptionList />
          </RequireAuth>
        }
      />
    </>
  );
}

export default Reception;
