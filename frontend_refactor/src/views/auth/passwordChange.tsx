import { passwordChangeRequest, verifyToken } from "@/apis/auth/auth";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import "@/styles/auth/Auth.css";

const PasswordChange = () => {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token") || "";

  const [loading, setLoading] = useState(true);
  const [verified, setVerified] = useState(false);

  const [form, setForm] = useState({ password: "", confirmPassword: "" });
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchPasswordChange = async () => {
      if (!token) {
        setMessage("유효하지 않은 접근입니다. 토큰이 없습니다.");
        setLoading(false);
        return;
      }

      try {
        const response = await verifyToken(token);
        const { code, message } = response;

        if (code == "SU") {
          setVerified(true);
          return;
        } else {
          setMessage(message);
          return;
        }
      } catch (error) {
        setMessage("서버 오류가 발생했습니다.");
        return;
      } finally {
        setLoading(false);
        return;
      }
    };
    fetchPasswordChange();
    setMessage("");
  }, [token, form]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setForm({ ...form, [name]: value });
  };

  const onButtonClick = async () => {
    const { password, confirmPassword } = form;
    if (!password || !confirmPassword) {
      setMessage("모든 항목을 입력하세요");
      return;
    }

    if (password !== confirmPassword) {
      setMessage("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      const response = await passwordChangeRequest(token, {
        password,
        confirmPassword,
      });

      const { code, message } = response;

      if (code == "SU") {
        alert("비밀번호가 성공적으로 변경되었습니다. 창을 닫습니다.");
        window.close();
        if (!closed) {
          alert(
            "창이 자동으로 닫히지 않으면 브라우저 탭을 수동으로 닫아주세요."
          );
        }
      } else {
        setMessage(message || "비밀번호 변경에 실패했습니다.");
      }
    } catch {
      setMessage("오류가 발생했습니다.");
    }
  };

  return (
    <div className="container">
      <img
        src="/src/apis/constants/북허브_로그_로그인창.png"
        alt="BookHub 로고"
        className="logo-img"
      />
      {!loading && verified && (
        <div className="form-box">
          <h2>비밀번호 변경</h2>
          <input
            type="password"
            placeholder="새 비밀번호"
            name="password"
            value={form.password}
            onChange={onInputChange}
            required
          />
          <input
            type="password"
            placeholder="새 비밀번호 확인"
            name="confirmPassword"
            value={form.confirmPassword}
            onChange={onInputChange}
            required
            />
            {message && <p className="failP">{message}</p>}
          <button onClick={onButtonClick}>비밀번호 변경</button>
        </div>
      )}
      {!loading && !verified && <p>{message}</p>}
    </div>
  );
};

export default PasswordChange;
