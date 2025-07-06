import { loginIdFindRequest } from "@/apis/auth/auth";
import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";

function LoginIdGet() {
  const [searchParams] = useSearchParams();
  const token = searchParams.get("token");

  const [loginId, setLoginId] = useState("");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchLoginId = async () => {
      if (!token) {
        setMessage("유효하지 않은 접근입니다. 토큰이 없습니다.");
        setLoading(false);
        return;
      }

      try {
        const response = await loginIdFindRequest(token);
        const { code, message, data } = response;

        if (code == "SU") {
          setLoginId(data || "null");
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

    fetchLoginId();
  }, [token]);

  const onCloseButtonClick = () => {
    alert("창을 닫습니다.");
    window.close();
    if (!closed) {
      alert("창이 자동으로 닫히지 않으면 브라우저 탭을 수동으로 닫아주세요.");
    }
  };

  return (
    <div className="container">
      <img
        src="/src/apis/constants/북허브_로그_로그인창.png"
        alt="BookHub 로고"
        className="logo-img"
      />
      {loading && <p>로그인 ID를 불러오는 중입니다...</p>}

      {!loading && loginId && (
        <div className="form-box">
          <h2>아이디 찾기</h2>
          <p className="getP">사원의 아이디는 <strong>{loginId}</strong> 입니다.</p>
          <button onClick={onCloseButtonClick}>확인</button>
        </div>
      )}
      {!loading && !loginId && (
        <p style={{ fontSize: "30px", margin: "10px" }}>{message}</p>
      )}
    </div>
  );
}

export default LoginIdGet;
