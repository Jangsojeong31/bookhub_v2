import React from "react";
import { Navigate, Route } from "react-router-dom";
import SignIn from "./SignIn";
import SignUp from "./SignUp";
import LoginIdFindEmail from "./loginIdFindEmail";
import LoginIdGet from "./loginIdGet";
import PasswordChange from "./passwordChange";
import PasswordChangeSendEmail from "./PasswordChangeSendEmail";
import EmployeeUpdate from "./EmployeeUpdate";

function Auth() {
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

export default Auth;
