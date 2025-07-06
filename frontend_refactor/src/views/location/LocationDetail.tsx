// // ------------------------------------------------------------
// // 📁 src/components/location/LocationDetail.tsx
// // ------------------------------------------------------------



// 📁 src/components/location/LocationDetail.tsx
import React, { useEffect, useRef, useState } from "react";
import { getLocationDetail } from "@/apis/location/location";
import { useCookies } from "react-cookie";
import { LocationDetailResponseDto } from "@/dtos/location/location.dto";

interface Props {
  locationId: number | null;
  branchId: number;
  open: boolean;
  onClose: () => void;
}

export function LocationDetail({ locationId, branchId, open, onClose }: Props) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [cookies] = useCookies(["accessToken"]);
  const [detail, setDetail] = useState<LocationDetailResponseDto | null>(null);

  useEffect(() => {
    if (open && locationId !== null) {
      (async () => {
        try {
          const res = await getLocationDetail(
            cookies.accessToken,
            branchId,
            locationId
          );
          // ResponseDto<T> 형태의 wrapper에서 실제 데이터 꺼내기
          if (res.data) {
            setDetail(res.data);
          }
        } catch (err) {
          console.error("진열 위치 상세 조회 실패:", err);
        }
      })();
    }
  }, [open, locationId, branchId, cookies.accessToken]);

  useEffect(() => {
    if (open) {
      dialogRef.current?.showModal();
    } else {
      dialogRef.current?.close();
      setDetail(null);
    }
  }, [open]);

  return (
    <dialog ref={dialogRef} onClose={onClose} className="p-4 rounded-md">
      <h3 className="text-xl font-semibold mb-4">책 위치 상세</h3>
      {detail ? (
        <ul className="space-y-2">
          <li><strong>제목:</strong> {detail.bookTitle}</li>
          <li><strong>층:</strong> {detail.floor}</li>
          <li><strong>홀:</strong> {detail.hall}</li>
          <li><strong>섹션:</strong> {detail.section}</li>
          <li><strong>타입:</strong> {detail.type}</li>
          {detail.note && <li><strong>비고:</strong> {detail.note}</li>}
        </ul>
      ) : (
        <p>로딩 중…</p>
      )}
      <div className="mt-6 text-right">
        <button
          onClick={onClose}
          className="px-4 py-2 bg-gray-200 rounded hover:bg-gray-300"
        >
          닫기
        </button>
      </div>
    </dialog>
  );
}

