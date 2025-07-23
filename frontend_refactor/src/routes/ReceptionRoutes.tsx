import { Route } from "react-router-dom";
import ReceptionConfirm from "../views/reception/ReceptionConfirm";
import ReceptionPending from "../views/reception/ReceptionPending";
import AdminReceptionList from "../views/reception/AdminReceptionList";
import RequireAuth from "@/components/auth/RequireAuth";

function ReceptionRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/reception/logs" element={<AdminReceptionList />}/>
      </Route>

      <Route element={<RequireAuth allowedRoles={["ADMIN", "MANAGER"]} />}>
        <Route path="/reception/confirmed" element={<ReceptionConfirm />}/>
        <Route path="/reception/pending" element={<ReceptionPending />}/>
      </Route>
    </>
  );
}

export default ReceptionRoutes;
