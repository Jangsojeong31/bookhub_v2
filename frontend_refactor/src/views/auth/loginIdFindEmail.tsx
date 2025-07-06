import { loginIdFindSendEmailRequest } from "@/apis/auth/auth";
import { LoginIdFindSendEmailRequestDto } from "@/dtos/auth/request/login-id-find-email.request.dto";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "@/styles/auth/Auth.css";

function LoginIdFindEmail() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: "",
    phoneNumber: "",
  });

  const [message, setMessage] = useState("");

  useEffect(() => {
    setMessage("");
    return;
  }, [form]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const onEmailSendClick = async () => {
    if (!form.email || !form.phoneNumber) {
      setMessage("모든 항목을 입력하세요");
      return;
    }

    const response = await loginIdFindSendEmailRequest(form);
    const { code, message, data } = response;

    if (code != "SU") {
      setMessage("이메일 전송 실패: " + message);
      return;
    } else {
      alert(data);
      navigate("/auth/login");
    }
  };

  const onLogoClick = () => {
    navigate("/auth/login");
  };

  return (
    <div className="container">
      <img
        src="/src/apis/constants/북허브_로그_로그인창.png"
        alt="BookHub 로고"
        className="logo-img"
        onClick={onLogoClick}
      />
      <div className="form-box">
        <h2>아이디 찾기</h2>
        <input
          type="email"
          placeholder="이메일"
          name="email"
          value={form.email}
          onChange={onInputChange}
        />
        <input
          type="tel"
          placeholder="전화번호"
          name="phoneNumber"
          value={form.phoneNumber}
          onChange={onInputChange}
        />
        {message && <p className="failP">{message}</p>}
        <button onClick={onEmailSendClick}>이메일 전송</button>
      </div>
    </div>
  );
}

export default LoginIdFindEmail;
