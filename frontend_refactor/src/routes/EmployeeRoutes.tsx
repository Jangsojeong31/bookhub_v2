import { Route, Routes } from "react-router-dom";
import EmployeeSearch from "../views/employee/EmployeeSearch";
import EmployeeSignUpApprovals from "../views/employee/employeeSignUpApprovals/employeeSignUpApprovals";
import EmployeeChange from "../views/employee/EmployeeChange";
import EmployeeSignUpApprovalsSearch from "../views/employee/employeeSignUpApprovals/EmployeeSignUpApprovalsSearch";
import EmployeeChangeLogsSearch from "../views/employee/employeeChangeLogs/EmployeeChangeLogsSearch";
import EmployeeExitLogs from "../views/employee/employeeExitLogs/employeeExitLogs";
import RequireAuth from "@/components/auth/RequireAuth";

function EmployeeRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/employees" element={<EmployeeSearch />}/>
        <Route path="/employees/approval" element={<EmployeeSignUpApprovals />}/>
        <Route path="/employees/edit" element={<EmployeeChange />}/>
        <Route path="/employees/approval/logs" element={<EmployeeSignUpApprovalsSearch />}/>
        <Route path="/employees/logs" element={<EmployeeChangeLogsSearch />}/>
        <Route path="/employees/retired/logs" element={<EmployeeExitLogs />}/>
      </Route>
    </>
  );
}

export default EmployeeRoutes;
