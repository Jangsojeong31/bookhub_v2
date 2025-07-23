import React from "react";
import { Navigate, Route } from "react-router-dom";
import SignIn from "../views/auth/SignIn";
import SignUp from "../views/auth/SignUp";
import LoginIdFindEmail from "../views/auth/loginIdFindEmail";
import LoginIdGet from "../views/auth/loginIdGet";
import PasswordChange from "../views/auth/passwordChange";
import PasswordChangeSendEmail from "../views/auth/PasswordChangeSendEmail";
import EmployeeUpdate from "../views/auth/EmployeeUpdate";

function AuthRoutes() {
  return (
    <>
      <Route path="/" element={<Navigate to="/auth/login" />} />
      <Route path="/auth/login" element={<SignIn />} />
      <Route path="/auth/sign-up" element={<SignUp />} />
      <Route path="/auth/login-id-find/email" element={<LoginIdFindEmail />} />
      <Route path="/auth/login-id-find" element={<LoginIdGet />} />
      <Route path="/auth/password-change" element={<PasswordChange />} />
      <Route
        path="/auth/password-change/email"
        element={<PasswordChangeSendEmail />}
      />
      <Route path="/auth/sign-up/update" element={<EmployeeUpdate />} />
    </>
  );
}

export default AuthRoutes;
