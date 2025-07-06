import {
  getDailySalesQuantity,
  getMonthlySalesQuantity,
} from "@/apis/statistics/salesQuantityStatistics/salesQuantityStatistics";
import React, { useEffect, useState } from "react";
import { useCookies } from "react-cookie";
import {
  ResponsiveContainer,
  BarChart,
  XAxis,
  YAxis,
  Tooltip,
  Bar,
  Cell,
} from "recharts";

type ChartData = { name: string; total: number };

function MonthlySalesQuantity() {
  const [cookies] = useCookies(["accessToken"]);
  const [chartData, setChartData] = useState<ChartData[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const thisYear = new Date().getFullYear();
  const yearRange = Array.from({ length: 5 }, (_, i) => thisYear - 4 + i).sort(
    (a, b) => b - a
  );
  const [selectedYear, setSelectedYear] = useState<number>(thisYear);

  const monthRange = Array.from({ length: 12 }, (_, i) => i + 1);

  const token = cookies.accessToken as string;

  // 새로고침하면 차트 갱신
  const onFetchChart = async () => {
    if (!token) return;
    setLoading(true);

    const response = await getMonthlySalesQuantity(selectedYear, token);
    const { code, message, data } = response;

    if (code != "SU") {
      alert(`${message}`)
      return;
    }

    if (Array.isArray(data)) {
      const mapped = monthRange.map((month) => {
        const foundData = data.find((item) => item.orderMonth === month);
        return {
          name: `${month}월`,
          total: foundData ? foundData.totalSales : 0,
        };
      });
      setChartData(mapped);
    }
    setLoading(false);
  };

  // 차트 처음 불러오기
  useEffect(() => {
    onFetchChart();
  }, [selectedYear]);

  return (
    <div
      style={{ width: "100%", maxWidth: 750, margin: "0 auto", padding: 10 }}
    >
      <h4>월별 통계</h4>

      <div style={{display: "flex", gap: 12, margin: 16}}>
        <select
          value={selectedYear}
          onChange={(e) => setSelectedYear(Number(e.target.value))}
          style={{ width: 150 }}
        >
          {yearRange.map((year) => (
            <option key={year} value={year}>
              {year}년
            </option>
          ))}
        </select>
        <div>

        <button onClick={onFetchChart} style={{ margin: 10}}>새로고침</button>
        </div>
      </div>

      {loading ? (
        <div>불러오는 중...</div>
      ) : (
        <ResponsiveContainer width="100%" height={240}>
          <BarChart data={chartData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="total">
              {chartData.map((data, idx) => (
                <Cell key={idx} cursor="pointer" fill="#FFBB28" />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  );
}

export default MonthlySalesQuantity;
