import { getDailySalesQuantity } from "@/apis/statistics/salesQuantityStatistics/salesQuantityStatistics";
import { eachDayOfInterval, lastDayOfMonth } from "date-fns";
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

function DailySalesQuantity() {
  const [cookies] = useCookies(["accessToken"]);
  const [chartData, setChartData] = useState<ChartData[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [selectedMonth, setSelectedMonth] = useState<number>(
    new Date().getMonth() + 1
  );

  const thisYear = new Date().getFullYear();

  let dayRange = eachDayOfInterval({
    start: new Date(new Date().getFullYear(), selectedMonth - 1, 1),
    end: lastDayOfMonth(new Date(new Date().getFullYear(), selectedMonth - 1)),
  });
  const start = new Date(thisYear, selectedMonth - 1, 1);
  const end = lastDayOfMonth(start);
  dayRange = eachDayOfInterval({ start, end });

  const token = cookies.accessToken as string;

  // 새로고침하면 차트 갱신
  const onFetchChart = async () => {
    if (!token) return;
    setLoading(true);

    const response = await getDailySalesQuantity(selectedMonth, token);
    const { code, message, data } = response;

    if (code != "SU") {
      alert(`${message}`)
      return;
    }

    if (Array.isArray(data)) {
      const mapped = dayRange.map((day) => {
        const foundData = data.find((item) => {
          const d = new Date(item.orderDate!);
          return d.toDateString() === day.toDateString(); // 날짜 완전 일치
        });

        return {
          name: `${selectedMonth}/${day.getDate()}`,
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
  }, [selectedMonth]);

  return (
    <div
      style={{ width: "100%", maxWidth: 1500, margin: "0 auto", padding: 32 }}
    >
      <h4>일일 통계</h4>

      <div style={{ display: "flex", gap: 12, margin: 16 }}>
        

        <select
          value={selectedMonth}
          onChange={(e) => setSelectedMonth(Number(e.target.value))}
          style={{width: 150}}
          >
          {[...Array(12)].map((_, idx) => (
            <option key={idx + 1} value={idx + 1}>
              {idx + 1}월
            </option>
          ))}
        </select>
        <button onClick={onFetchChart}
        style={{margin: 10}}>새로고침</button>
      </div>

      {loading ? (
        <div>불러오는 중...</div>
      ) : (
        <ResponsiveContainer width="100%" height={240}>
          <BarChart data={chartData}>
            <XAxis dataKey="name" angle={-30} textAnchor="end" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="total">
              {chartData.map((data, idx) => (
                <Cell key={idx} cursor="pointer" fill="#0088FE" />
              ))}
            </Bar>
          </BarChart>
        </ResponsiveContainer>
      )}
    </div>
  );
}

export default DailySalesQuantity;
