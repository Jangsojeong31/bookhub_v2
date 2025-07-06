// views/category/UpdateCategory.tsx

import { useEffect, useState } from "react";
import { CategoryUpdateRequestDto } from "@/dtos/category/request/category-update.request.dto";
import { updateCategory } from "@/apis/category/category";
import { CategoryTreeResponseDto } from "@/dtos/category/response/category-tree.response.dto";
import { useCookies } from "react-cookie";

interface UpdateCategoryProps {
  category: CategoryTreeResponseDto;
  onSuccess: () => void;
  mode: "update" | "delete";
}

function UpdateCategory({ category, onSuccess, mode }: UpdateCategoryProps) {
  const [categoryName, setCategoryName] = useState("");
  const [categoryType, setCategoryType] = useState<"DOMESTIC" | "FOREIGN">("DOMESTIC");
  const [discountPolicyId, setDiscountPolicyId] = useState<number | null>(null);
  const [isActive, setIsActive] = useState(true);
  const [cookies] = useCookies(["accessToken"]);

  useEffect(() => {
    setCategoryName(category.categoryName);
    setCategoryType(category.categoryType);
    setIsActive(category.isActive);
    setDiscountPolicyId(category.discountPolicyId ?? null);
  }, [category]);

  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();

    const token = cookies.accessToken;
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    const dto: CategoryUpdateRequestDto = {
      categoryName,
      categoryType,
      isActive,
      ...(discountPolicyId !== null ? { discountPolicyId } : {}),
    };

    try {
      await updateCategory(category.categoryId, dto, token);
      alert("카테고리 수정 성공!");
      onSuccess();
    } catch (err) {
      alert("카테고리 수정 실패");
    }
  };

  const handleDeactivate = async () => {
    const confirm = window.confirm("정말 이 카테고리를 비활성화하시겠습니까?");
    if (!confirm) return;

    const token = cookies.accessToken;
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    const dto: CategoryUpdateRequestDto = {
      categoryName,
      categoryType,
      isActive: false,
    };
    
    try {
      await updateCategory(category.categoryId, dto, token);
      alert("카테고리 비활성화 완료");
      onSuccess();
    } catch (error) {
      alert("비활성화 실패");
    }
  };

  return (
    <form onSubmit={handleUpdate} style={{ display: "flex", flexDirection: "column", gap: "12px" }}>
      <h3>✏️ 카테고리 수정</h3>

      <input
        type="text"
        value={categoryName}
        onChange={(e) => setCategoryName(e.target.value)}
        placeholder="카테고리 이름"
        required
      />

      <select value={categoryType} onChange={(e) => setCategoryType(e.target.value as "DOMESTIC" | "FOREIGN")}>
        <option value="DOMESTIC">국내도서</option>
        <option value="FOREIGN">해외도서</option>
      </select>

      <input
        type="number"
        value={discountPolicyId ?? ""}
        onChange={(e) => setDiscountPolicyId(Number(e.target.value))}
        placeholder="할인 정책 ID"
      />

      <label>
        <input
          type="checkbox"
          checked={isActive}
          onChange={(e) => setIsActive(e.target.checked)}
        />
        활성 상태
      </label>

      { mode === "update" && <button type="submit">수정</button>}
      { mode === "delete" && <button type="button" onClick={handleDeactivate} style={{
        backgroundColor: "#ffdddd", color: "red" 
      }}>비활성화(삭제)</button>}
    </form>
  );
}

export default UpdateCategory;
