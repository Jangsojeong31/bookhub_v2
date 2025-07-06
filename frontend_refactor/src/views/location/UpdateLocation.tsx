import { DisplayType } from "@/apis/enums/DisplayType";
import { getLocationDetail, updateLocation } from "@/apis/location/location";
import { LocationUpdateRequestDto } from "@/dtos/location/location.dto";
import { useEmployeeStore } from "@/stores/employee.store";
import { useRef, useState, useEffect } from "react";
import { useCookies } from "react-cookie";

interface Props {
  locationId: number | null;
  open: boolean;
  onClose: () => void;
  onSuccess: () => Promise<void>;
}

export function UpdateLocation({
  locationId,
  open,
  onClose,
  onSuccess,
}: Props) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [cookies] = useCookies(["accessToken"]);
  const employee = useEmployeeStore((state) => state.employee);
  const branchId = employee?.branchId ?? null;

  const [form, setForm] = useState<LocationUpdateRequestDto>({
    floor: undefined,
    hall: "",
    section: "",
    displayType: undefined,
    note: "",
  });

  // 1) 모달이 열릴 때 기존 상세 데이터를 불러와서 form에 채워주기
  useEffect(() => {
    if (open && locationId != null && branchId != null) {
      (async () => {
        try {
          const res = await getLocationDetail(
            cookies.accessToken,
            branchId,
            locationId
          );
          if (res.data) {
            setForm({
              floor: res.data.floor,
              hall: res.data.hall,
              section: res.data.section,
              displayType: res.data.type,
              note: res.data.note ?? "",
            });
          }
        } catch (err) {
          console.error("진열 위치 상세 조회 실패:", err);
        }
      })();
    }
  }, [open, locationId, branchId, cookies.accessToken]);

  // 2) open 상태에 따라 dialog 열기/닫기, 닫힐 때는 form 초기화
  useEffect(() => {
    if (open) {
      dialogRef.current?.showModal();
    } else {
      dialogRef.current?.close();
      setForm({
        floor: undefined,
        hall: "",
        section: "",
        displayType: undefined,
        note: "",
      });
    }
  }, [open]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({
      ...prev,
      [name]:
        name === "floor"
          ? Number(value)
          : name === "displayType"
          ? (value as DisplayType)
          : value,
    }));
  };

  // 3) 수정 제출 핸들러: branchId까지 넘겨주기
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (locationId == null || branchId == null) return;

    try {
      await updateLocation(
        locationId,
        form,
        cookies.accessToken,
        branchId
      );
      await onSuccess();
      onClose();
    } catch (err) {
      console.error("진열 위치 수정 실패:", err);
    }
  };

  return (
    <dialog
      ref={dialogRef}
      onClose={onClose}
      className="p-6 rounded-lg max-w-md w-full"
    >
      <h3 className="text-xl font-semibold mb-4">책 위치 수정</h3>

      {form.floor !== undefined ? (
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1">
              층
              <input
                name="floor"
                type="number"
                value={form.floor}
                onChange={handleChange}
                className="mt-1 block w-full border rounded px-2 py-1"
              />
            </label>
          </div>
          <div>
            <label className="block mb-1">
              홀
              <input
                name="hall"
                value={form.hall}
                onChange={handleChange}
                className="mt-1 block w-full border rounded px-2 py-1"
              />
            </label>
          </div>
          <div>
            <label className="block mb-1">
              섹션
              <input
                name="section"
                value={form.section}
                onChange={handleChange}
                className="mt-1 block w-full border rounded px-2 py-1"
              />
            </label>
          </div>
          <div>
            <label className="block mb-1">
              진열 타입
              <select
                name="displayType"
                value={form.displayType}
                onChange={handleChange}
                className="mt-1 block w-full border rounded px-2 py-1"
              >
                {Object.entries(DisplayType).map(([key, val]) => (
                  <option key={key} value={val}>
                    {val}
                  </option>
                ))}
              </select>
            </label>
          </div>
          <div>
            <label className="block mb-1">
              비고
              <input
                name="note"
                value={form.note??""}
                onChange={handleChange}
                className="mt-1 block w-full border rounded px-2 py-1"
              />
            </label>
          </div>

          <div className="flex justify-end space-x-2 mt-6">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
            >
              취소
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
            >
              수정
            </button>
          </div>
        </form>
      ) : (
        <p>로딩 중…</p>
      )}
    </dialog>
  );
}