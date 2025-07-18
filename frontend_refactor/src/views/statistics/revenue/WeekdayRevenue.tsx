import { fetchWeekday } from "@/apis/statistics/revenue/revenueStatistics";
import { ResponseDto } from "@/dtos";
import { WeekdayRevenueResponseDto } from "@/dtos/statistics/revenue/revenue.response";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { BarChart, CartesianGrid, XAxis, YAxis, Tooltip, Bar } from "recharts";

function WeekdayRevenue() {
  const [cookies] = useCookies(['accessToken']);
  const token = cookies.accessToken;
  const [year, setYear] = useState<number>(new Date().getFullYear());
  const [quarter, setQuarter] = useState<number>(1);
  const [data, setData] = useState<WeekdayRevenueResponseDto[]>([]);
  const [loading, setLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const defaultWeekdays = [
    {weekday: '월', totalRevenue: 0},
    {weekday: '화', totalRevenue: 0},
    {weekday: '수', totalRevenue: 0},
    {weekday: '목', totalRevenue: 0},
    {weekday: '금', totalRevenue: 0},
    {weekday: '토', totalRevenue: 0},
    {weekday: '일', totalRevenue: 0}
  ];

  const loadData = async () => {
    if (!token) {
      setError('로그인이 필요합니다.');
      return;
    }
    setLoading(true);
    setError(null);
    const res: ResponseDto<WeekdayRevenueResponseDto[]> = await fetchWeekday(token, year, quarter);
    if (res.code === 'SU') {
      const mergedData = defaultWeekdays.map((d) => {
        const match = res.data!.find((item) => item.weekday === d.weekday);
        return {...d, totalRevenue: match ? match.totalRevenue : 0};
      })
      setData(mergedData ?? []);
    } else {
      setError(res.message);
    }
    setLoading(false);
  };



  useEffect(() => {
    loadData();
  }, [year, quarter]);

  return (
    <div 
    style={{ width: "100%", maxWidth: 900, height: 300, margin: "0 100px", padding: 20 }}>
      <h3>요일별 매출 통계</h3>
      <div className="flex items-center gap-4 mb-4">
        <label className="flex items-center">
          연도:
          <input
            type="number"
            value={year}
            onChange={(e) => setYear(Number(e.target.value))}
            className="border rounded p-1 ml-2 w-20"
          />
        </label>
        <div className="flex items-center">
          <span className="mr-2">분기:</span>
          {[1, 2, 3, 4].map((q) => (
            <button
              key={q}
              className={`px-3 py-1 border rounded mr-2 ${quarter === q ? 'bg-blue-500 text-white' : ''}`}
              onClick={() => setQuarter(q)}
            >
              {q}분기
            </button>
          ))}
        </div>
      </div>
      {loading && <p>로딩 중...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!loading && !error && (
        <BarChart width={600} height={300} data={data} className="mx-auto">
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="weekday" />
          <YAxis />
          <Tooltip />
          <Bar dataKey="totalRevenue" fill="#8884d8" />
        </BarChart>
      )}
    </div>
  );
};

export default WeekdayRevenue;