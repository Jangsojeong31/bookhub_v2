import { fetchBranchRevenue } from "@/apis/statistics/revenue/revenueStatistics";
import { ResponseDto } from "@/dtos";
import { BranchRevenueResponseDto } from "@/dtos/statistics/revenue/revenue.response";
import { useState } from "react";
import { useCookies } from "react-cookie";
import { NavLink } from "react-router-dom";
import { BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Legend, Bar } from "recharts";

interface FormData {
  startDate: string;
  endDate: string;
}

const COLOR_PALETTE = [
  "#8884d8",
  "#82ca9d",
  "#ffc658",
  "#ff8042",
  "#8dd1e1",
  "#a4de6c",
  "#d0ed57",
  "#d066e1"
  // 필요하면 더 추가
];

function BranchRevenue() {
  const [cookies] = useCookies(['accessToken']);
  const token = cookies.accessToken;
  const [form, setForm] = useState<FormData>({ startDate: '', endDate: '' });
  const [data, setData] = useState<any[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    if (!token) {
      setError('로그인이 필요합니다.');
      return;
    }
    if (!form.startDate || !form.endDate) {
      setError('시작일과 종료일을 선택해주세요.');
      return;
    }
    setLoading(true);
    setError(null);
    const res: ResponseDto<BranchRevenueResponseDto[]> = await fetchBranchRevenue(
      token,
      form.startDate,
      form.endDate
    );
    if (res.code === 'SU') {
const raw: BranchRevenueResponseDto[] = res.data ?? [];      const branchMap: Record<string, any> = {};
      const cats = new Set<string>();
      raw.forEach((item) => {
        const name = item.branchName;
        if (!branchMap[name]) branchMap[name] = { branchName: name };
        branchMap[name][item.categoryName] = item.totalRevenue;
        cats.add(item.categoryName);
      });
      setCategories(Array.from(cats));
      setData(Object.values(branchMap));
    } else {
      setError(res.message);
    }
    setLoading(false);
  };

  return (
    <div>
      <h2>매출 통계</h2>
          <div style={{ marginBottom: 16, display: "flex", gap: 12 }}>
              {[
                { to: "/statistics/revenue/period", label: "기간별" },
                { to: "/statistics/revenue/branch", label: "지점별" },
              ].map(({ to, label }) => (
                <NavLink
                  key={to}
                  to={to}
                  style={({ isActive }) => ({
                    backgroundColor: isActive ? "#265185" : "#f0f0f0",
                    color: isActive ? "white" : "#333",
                    padding: "10px 20px",
                    borderRadius: 6,
                    textDecoration: "none",
                    fontWeight: isActive ? "bold" : "normal",
                    transition: "background-color 0.3s",
                  })}
                >
                  {label}
                </NavLink>
              ))}
            </div>

      <div className="flex items-center gap-4 mb-4">
        <label className="flex items-center">
          시작일:
          <input
            type="date"
            name="startDate"
            value={form.startDate}
            onChange={handleChange}
            className="border rounded p-1 ml-2"
          />
        </label>
        <label className="flex items-center">
          종료일:
          <input
            type="date"
            name="endDate"
            value={form.endDate}
            onChange={handleChange}
            className="border rounded p-1 ml-2"
          />
        </label>
        <button
          onClick={handleSubmit}
          className="ml-2 px-4 py-1 bg-blue-500 text-white rounded"
        >
          조회
        </button>
      </div>
      {loading && <p>로딩 중...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && data.length > 0 && (
        <BarChart width={1500}
            height={600}
            data={data}
            style={{margin: "40px auto"}}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="branchName" angle={-30} textAnchor="end" height={100} />
          <YAxis width={100} />
          <Tooltip />
          <Legend layout="vertical" verticalAlign="middle" align="right"/>
          {categories.map((cat,idx) => (
            <Bar key={cat} dataKey={cat} stackId="a"
            fill={COLOR_PALETTE[idx % COLOR_PALETTE.length]} />
          ))}
        </BarChart>
      )}
    </div>
  );
};

export default BranchRevenue;
