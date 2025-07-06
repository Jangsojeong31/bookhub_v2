/** @jsxImportSource @emotion/react */
import { checkLoginIdDuplicate, signUpRequest } from "@/apis/auth/auth";
import { GET_BRANCH_URL } from "@/apis/constants/khj.constants";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "@/styles/auth/Auth.css";
import { Branch } from "@/dtos/branch/branch";

function SignUp() {
  const navigate = useNavigate();
  const [branches, setBranches] = useState<Branch[]>([]);
  const [form, setForm] = useState({
    loginId: "",
    password: "",
    confirmPassword: "",
    name: "",
    email: "",
    phoneNumber: "",
    birthDate: "",
    branchId: 0,
  });

  const loginRegex = /^[A-Za-z][A-Za-z\d]{3,12}/;
  const [loginIdFailMessage, setLoginIdFailMessage] = useState("");
  const [loginIdSuccessMessage, setLoginIdSuccessMessage] = useState("");

  const passwordRegex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%*?])[A-Za-z\d!@#$%*?]{8,16}$/;
  const [message, setMessage] = useState("");
  const [passwordFailCheckMessage, setPasswordFailCheckMessage] = useState("");
  const [passwordSuccessCheckMessage, setPasswordSuccessCheckMessage] =
    useState("");

  const emailRegex = /^[A-Za-z][A-Za-z\d]+@[A-Za-z\d.-]+\.[A-Za-z]{2,}$/;

  useEffect(() => {
    setLoginIdFailMessage("");
    setLoginIdSuccessMessage("");
  }, [form.loginId]);

  useEffect(() => {
    setMessage("");
  }, [form]);

  useEffect(() => {
    setPasswordSuccessCheckMessage("");
    setPasswordFailCheckMessage("");
  }, [form.password, form.confirmPassword]);

  useEffect(() => {
    fetch(`${GET_BRANCH_URL}?branchLocation`, {
      method: "GET",
    })
      .then((res) => res.json())
      .then((data) => {
        setBranches(data.data);
      })
      .catch((e) => console.error(e));
  }, []);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const onSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectBranchId = e.target.value;
    setForm({ ...form, branchId: Number(selectBranchId) });
  };

  const onPasswordBlur = async () => {
    if (form.password) {
      if (!passwordRegex.test(form.password)) {
        setPasswordFailCheckMessage(
          "8~16자 영문, 숫자, 특수문자 모두 포함되어야 합니다."
        );
        return;
      }
    }
    if (form.confirmPassword && form.password) {
      if (form.password !== form.confirmPassword) {
        setPasswordFailCheckMessage("비밀번호가 일치하지 않습니다.");
        return;
      } else {
        setPasswordSuccessCheckMessage("비밀번호가 일치합니다.");
        return;
      }
    }
  };

  const onCheckLoginIdBlur = async () => {
    if (!loginRegex.test(form.loginId)) {
      setLoginIdFailMessage("아이디는 4~12자의 영어와 숫자만 사용해야 합니다.");
      return;
    }

    const response = await checkLoginIdDuplicate(form.loginId);
    const { code, message } = response;

    if (code == "SU") {
      setLoginIdSuccessMessage(message);
      return;
    }
    setLoginIdFailMessage(message);
  };

  const onSignUpClick = async () => {
    if (!form.birthDate && (!form.branchId || form.branchId === 0)) {
      setMessage("모든 항목을 입력해 주세요");
      return;
    } else if (!form.birthDate) {
      setMessage("생일을 입력해 주세요");
      return;
    } else if (!form.branchId || form.branchId === 0) {
      setMessage("지점을 선택해 주세요");
      return;
    }

    if (!emailRegex.test(form.email)) {
      setMessage("이메일 형식이 아닙니다.");
      return;
    }

    const response = await signUpRequest(form);
    const { code, message } = response;

    if (code !== "SU") {
      alert("회원가입에 실패");
      if (code === "NMPW" || code === "DI") {
        return;
      } else {
        setMessage(message);
      }
      console.log(code + " " + message);
      return;
    } else {
      alert("회원가입을 성공하였습니다.");
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
        <h2>SIGN UP</h2>
        <input
          type="text"
          placeholder="아이디 (영문으로 시작, 4~12자 영문/숫자 조합)"
          name="loginId"
          value={form.loginId}
          onChange={onInputChange}
          onBlur={onCheckLoginIdBlur}
        />
        <br />
        {loginIdFailMessage && <p className="failP">{loginIdFailMessage}</p>}
        {loginIdSuccessMessage && (
          <p className="successP">{loginIdSuccessMessage}</p>
        )}
        <input
          type="password"
          placeholder="비밀번호 (8~16자 영문, 숫자, 특수문자 모두 포함)"
          name="password"
          value={form.password}
          onChange={onInputChange}
          onBlur={onPasswordBlur}
        />
        <br />
        <input
          type="password"
          placeholder="비밀번호 확인"
          name="confirmPassword"
          value={form.confirmPassword}
          onChange={onInputChange}
          onBlur={onPasswordBlur}
        />
        <br />
        {passwordFailCheckMessage && (
          <p className="failP">{passwordFailCheckMessage}</p>
        )}
        {passwordSuccessCheckMessage && (
          <p className="successP">{passwordSuccessCheckMessage}</p>
        )}
        <input
          type="text"
          placeholder="이름"
          name="name"
          value={form.name}
          onChange={onInputChange}
        />
        <br />
        <input
          type="email"
          placeholder="이메일"
          name="email"
          value={form.email}
          onChange={onInputChange}
        />
        <br />
        <input
          type="tel"
          placeholder="전화번호"
          name="phoneNumber"
          value={form.phoneNumber}
          onChange={onInputChange}
        />
        <br />
        <input
          type="date"
          placeholder="생년월일"
          name="birthDate"
          value={form.birthDate}
          onChange={onInputChange}
        />
        <br />
        <select
          value={form.branchId}
          onChange={onSelectChange}
          className="custom-select"
        >
          <option value={0}>지점을 선택하세요</option>
          {branches.map((branch) => (
            <option key={branch.branchId} value={branch.branchId}>
              {branch.branchName}
            </option>
          ))}
        </select>
        <br />
        {message && <p className="failP">{message}</p>}
        <button onClick={onSignUpClick}>회원가입</button>
      </div>
    </div>
  );
}

export default SignUp;
