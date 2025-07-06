import { Route } from "react-router-dom";
import EmployeeSearch from "./EmployeeSearch";
import EmployeeSignUpApprovals from "./employeeSignUpApprovals/employeeSignUpApprovals";
import EmployeeChange from "./EmployeeChange";
import EmployeeSignUpApprovalsSearch from "./employeeSignUpApprovals/EmployeeSignUpApprovalsSearch";
import EmployeeChangeLogsSearch from "./employeeChangeLogs/EmployeeChangeLogsSearch";
import EmployeeExitLogs from "./employeeExitLogs/employeeExitLogs";
import RequireAuth from "@/components/auth/RequireAuth";

function Employee() {
  return (
    <>
      <Route
        path="/employees"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeSearch />
          </RequireAuth>
        }
      />
      <Route
        path="/employees/approval"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeSignUpApprovals />
          </RequireAuth>
        }
      />
      <Route
        path="/employees/edit"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeChange />
          </RequireAuth>
        }
      />
      <Route
        path="/employees/approval/logs"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeSignUpApprovalsSearch />
          </RequireAuth>
        }
      />
      <Route
        path="/employees/logs"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeChangeLogsSearch />
          </RequireAuth>
        }
      />
      <Route
        path="/employees/retired/logs"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <EmployeeExitLogs />
          </RequireAuth>
        }
      />
    </>
  );
}

export default Employee;
