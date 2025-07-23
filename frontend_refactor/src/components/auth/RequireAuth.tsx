import { useEmployeeStore } from "@/stores/useEmployeeStore";
import { Navigate, Outlet, useLocation } from "react-router-dom";

type RequireRoleProps = {
  allowedRoles: string[];
};

function RequireAuth(props: RequireRoleProps) {
  const { allowedRoles } = props;
  const employee = useEmployeeStore((state) => state.employee);
  const location = useLocation();

  if (!employee || !allowedRoles.includes(employee.authorityName)) {
    alert("권한이 없습니다.");
    return <Navigate to="/main" replace state={{ from: location }} />;
  }

  return <Outlet />;
}

export default RequireAuth;
