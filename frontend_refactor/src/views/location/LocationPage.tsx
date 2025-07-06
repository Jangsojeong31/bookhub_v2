import { getLocations, deleteLocation } from "@/apis/location/location";
import { LocationResponseDto } from "@/dtos/location/location.dto";
import { useEmployeeStore } from "@/stores/employee.store";
import { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { CreateLocation } from "./CreateLocation";
import { LocationTable } from "./LocationTable";
import { UpdateLocation } from "./UpdateLocation";

export default function LocationPage() {
  const [cookies] = useCookies(["accessToken"]);
  const branchId = useEmployeeStore((state) => state.employee?.branchId);
  const [data, setData] = useState<LocationResponseDto[]>([]);
  const [keyword, setKeyword] = useState("");
  const [createOpen, setCreateOpen] = useState(false);
  const [updateOpen, setUpdateOpen] = useState(false);
  const [detailOpen, setDetailOpen] = useState(false);
  const [selectedId, setSelectedId] = useState<number | null>(null);
  const employee = useEmployeeStore((state) => state.employee);

  // 지점별 진열 위치 조회
  const fetchData = async () => {
    if (!branchId) return;
    const res = await getLocations(cookies.accessToken, branchId, keyword);
    if (res.data) setData(res.data);
  };

  useEffect(() => {
    fetchData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [branchId]);

  // 검색 핸들러
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    fetchData();
  };

  // 삭제 핸들러
  const handleDelete = async (id: number) => {
    if (!branchId) return;
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    await deleteLocation(id, cookies.accessToken, branchId);
    fetchData();
  };

  return (
    <section style={{ padding: "1rem" }}>
      <h1>책 진열 위치 관리</h1>

      {/* 검색 + 등록 */}
      <form onSubmit={handleSearch} style={{ marginBottom: "1rem" }}>
        <input
          placeholder="책 제목 검색"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          style={{ marginRight: "0.5rem" }}
        />
        <button type="submit" style={{ marginRight: "0.5rem" }}>
          검색
        </button>
        <button
          type="button"
          onClick={() => {
            if (
              employee?.authorityName == "MANAGER" ||
              employee?.authorityName == "ADMIN"
            ) {
              setCreateOpen(true);
            } else {
              alert("권한이 없습니다.");
            }
          }}
        >
          등록
        </button>
      </form>

      {/* 목록 테이블 */}
      <LocationTable
        data={data}
        onView={(id) => {
          setSelectedId(id);
          setDetailOpen(true);
        }}
        onEdit={(id) => {
          setSelectedId(id);
          setUpdateOpen(true);
        }}
        onDelete={handleDelete}
      />

      {/* 모달 컴포넌트들 */}
      <CreateLocation
        open={createOpen}
        onClose={() => setCreateOpen(false)}
        onSuccess={fetchData}
      />
      <UpdateLocation
        locationId={selectedId}
        open={updateOpen}
        onClose={() => setUpdateOpen(false)}
        onSuccess={fetchData}
      />
 
    </section>
  );
}
