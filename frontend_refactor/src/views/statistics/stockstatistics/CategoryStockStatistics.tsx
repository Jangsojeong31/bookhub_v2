import { GET_BRANCH_URL } from "@/apis";
import { categoryStockRequest } from "@/apis/statistics/stocksStatistics/stocksStatistics";
import { CategoryStockResponseDto } from "@/dtos/statistics/StocksStatistics/response/categoryStock.response.dto";
import { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import { NavLink } from "react-router-dom";
import { Cell, Legend, Pie, PieChart, Tooltip } from "recharts";

interface Branch {
  branchId: number;
  branchName: string;
}

const COLORS = [
  "#0088FE",
  "#00C49F",
  "#FFBB28",
  "#FF8042",
  "#AA336A",
  "#33AA99",
  "#AA6633",
  "#9966AA",
  "#66AA99",
  "#3366AA",
];

function calculatePercentages(data: CategoryStockResponseDto[]) {
  const total = data.reduce((sum, item) => sum + item.quantity, 0);
  return data
    .map((item) => ({
      ...item,
      percent: total === 0 ? 0 : (item.quantity / total) * 100,
    }))
    .sort((a, b) => b.percent - a.percent)
    .map((item, index) => ({
      ...item,
      rank: index + 1,
    }));
}

function CategoryStockStatistics() {
  const [cookies] = useCookies(["accessToken"]);
  const token = cookies.accessToken;

  const [searchParams, setSearchParams] = useState({ branchName: "" });
  const [branches, setBranches] = useState<Branch[]>([]);
  const [data, setData] = useState<
    (CategoryStockResponseDto & { percent: number })[]
  >([]);

  const [loading, setLoading] = useState(false);

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

  const onBranchNameChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const { name, value } = e.target;
    setSearchParams({ ...searchParams, [name]: value });
  };

  const onSearchClick = async () => {
    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    if (!searchParams.branchName) {
      alert("지점을 선택하세요");
      return;
    }

    setLoading(true);

    const response = await categoryStockRequest(searchParams, token);
    const { code, message, data } = response;

    if (code === "SU" && data) {
      setData(calculatePercentages(data));
      console.log(calculatePercentages(data));
    } else {
      console.error(message);
    }

    setLoading(false);
  };

  if (loading) {
    return <p>로딩 중...</p>;
  }

  return (
    <>
    <div>
        <h2>재고 통계</h2>
        <div style={{ marginBottom: 16, display: "flex", gap: 12 }}>
          {[
            { to: "/statistics/stocks/branch", label: "지점별" },
            { to: "/statistics/stocks/category", label: "카테고리별" },
            {
              to: "/statistics/stocks/time",
              label: "월별",
            },
            { to: "/statistics/stocks/zero", label: "재고 개수별" },
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
      </div>
      <div>
        <h3>지점별 각 카테고리의 재고 비율</h3>
        <select
          name="branchName"
          value={searchParams.branchName}
          onChange={onBranchNameChange}
        >
          <option value="">지점을 선택하세요.</option>
          {branches.map((branch) => (
            <option key={branch.branchId} value={branch.branchName}>
              {branch.branchName}
            </option>
          ))}
        </select>
        <button onClick={onSearchClick}>조회</button>
      </div>
      <div
        style={{
          width: "100%",
          height: "700px",
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
        }}
      >
        <div>
          {data.length > 0 && (
            <PieChart width={600} height={600}>
              <Pie
                data={data}
                dataKey="percent"
                nameKey="categoryName"
                cx="50%"
                cy="50%"
                outerRadius={180}
                label={({ categoryName, percent, rank }) =>
                  `${rank}. ${categoryName} ${percent.toFixed(1)}%`
                }
              >
                {data.map((entry, index) => (
                  <Cell
                    key={`cell-${index}`}
                    fill={COLORS[index % COLORS.length]}
                  />
                ))}
              </Pie>
              <Tooltip
                formatter={(value: number, name: string) => [
                  `${value.toFixed(1)}%`,
                  name,
                ]}
              />
              <Legend />
            </PieChart>
          )}
        </div>
      </div>
    </>
  );
}

export default CategoryStockStatistics;
