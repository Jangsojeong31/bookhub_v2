import { passwordChangeEmailRequest } from "@/apis/auth/auth";
import { PasswordChangeEamilRequestDto } from "@/dtos/auth/request/password-change-email.request.dto";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "@/styles/auth/Auth.css";

function PasswordChangeSendEmail() {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    loginId: "",
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
    const { loginId, email, phoneNumber } = form;

    if (!loginId || !email || !phoneNumber) {
      setMessage("모든 항목을 입력해주세요.");
      return;
    }

    const responseBody: PasswordChangeEamilRequestDto = {
      loginId,
      email,
      phoneNumber,
    };
    const response = await passwordChangeEmailRequest(responseBody);
    const { code, message, data } = response;

    if (code != "SU") {
      setMessage("이메일 전송 실패: " + message);
      console.log(code);
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
        onClick={onLogoClick}
        className="logo-img"
      />
      <div className="form-box">
        <h2>비밀번호 변경</h2>
        <input
          type="text"
          placeholder="아이디"
          name="loginId"
          value={form.loginId}
          onChange={onInputChange}
        />
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

export default PasswordChangeSendEmail;
