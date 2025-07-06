import { DisplayType } from "@/apis/enums/DisplayType";
import { createLocation } from "@/apis/location/location";
import { LocationCreateRequestDto } from "@/dtos/location/location.dto";
import { useEmployeeStore } from "@/stores/employee.store";
import { useRef, useState, useEffect } from "react";
import { useCookies } from "react-cookie";

interface Props {
  open: boolean;
  onClose: () => void;
  onSuccess: () => Promise<void>;
}

export function CreateLocation({ open, onClose, onSuccess }: Props) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  const [cookies] = useCookies(["accessToken"]);
  // 로그인된 직원에서 branchId 꺼내기
  const branchId = useEmployeeStore(state => state.employee?.branchId);

  const [form, setForm] = useState<LocationCreateRequestDto>({
    bookIsbn: "",
    floor: "",
    hall: "",
    section: "",
    displayType: DisplayType.BOOK_SHELF,
    note: null,
  });

  useEffect(() => {
    if (open) dialogRef.current?.showModal();
    else dialogRef.current?.close();
  }, [open]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setForm(prev => ({ ...prev, [name]: value }));
  };

  const submit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!branchId) {
      alert("지점 정보가 없습니다.");
      return;
    }
    await createLocation(form, cookies.accessToken, branchId);
    await onSuccess();
    onClose();
  };

  return (
    <dialog ref={dialogRef} onClose={onClose}>
      <h3>책 위치 등록</h3>
      <form onSubmit={submit}>
        <div>
          <label>
            ISBN:
            <input
              name="bookIsbn"
              value={form.bookIsbn}
              onChange={handleChange}
              required
            />
          </label>
        </div>
        <div>
          <label>
            층:
            <input name="floor" value={form.floor} onChange={handleChange} />
          </label>
        </div>
        <div>
          <label>
            홀:
            <input name="hall" value={form.hall} onChange={handleChange} />
          </label>
        </div>
        <div>
          <label>
            섹션:
            <input name="section" value={form.section} onChange={handleChange} />
          </label>
        </div>
        <div>
          <label>
            진열 타입:
            <select
              name="displayType"
              value={form.displayType}
              onChange={handleChange}
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
          <label>
            비고:
            <input
              name="note"
              value={form.note ?? ""}
              onChange={handleChange}
            />
          </label>
        </div>
        <div style={{ marginTop: "1rem" }}>
          <button type="button" onClick={onClose}>
            취소
          </button>
          <button type="submit">등록</button>
        </div>
      </form>
    </dialog>
  );
}
