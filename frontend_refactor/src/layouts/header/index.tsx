import React, { useEffect } from "react";
import styles from "./Header.module.css";
import { logoutRequest } from "@/apis/auth/auth";
import { useNavigate } from "react-router-dom";
import { useCookies } from "react-cookie";
import { useEmployeeStore } from "@/stores/employee.store";
import AlertIcon from "@/apis/constants/AlertIcon";

export default function Header() {
  const [cookies, , removeCookie] = useCookies(["accessToken", "tokenExpiresAt"]);
  const logout = useEmployeeStore((state) => state.setLogout);
  const employee = useEmployeeStore((state) => state.employee);
  const clearEmployee = useEmployeeStore((state) => state.clearEmployee);

  const navigate = useNavigate();
  const onLogoutClick = async () => {
    await logoutRequest();
    removeCookie("accessToken", { path: "/" });
    removeCookie("tokenExpiresAt", { path: "/" });
    clearEmployee();
    logout();
    navigate("/auth/login");
  };

  const token = cookies.accessToken;
  console.log(token);

  const onLogoClick = () => {
    navigate("/main");
  };

  return (
    <header className={styles.header}>
      <div className={styles.logo}>
        <img
          src="/src/apis/constants/북허브_로고_배경제거_navy.png"
          alt="BookHub 로고"
          onClick={onLogoClick}
          className={styles.logoImg}
        />
      </div>
      <div className={styles.headerInfo}>
        <AlertIcon />
        <div>
          {employee?.branchName} {employee?.positionName}{" "}
          {employee?.employeeName}
        </div>
        <button onClick={onLogoutClick}>로그아웃</button>
      </div>
    </header>
  );
}
