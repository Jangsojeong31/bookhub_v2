/** @jsxImportSource @emotion/react */
import React, { useState } from "react";
import { CategoryTreeResponseDto } from "@/dtos/category/response/category-tree.response.dto";
import {
  deleteCategory,
  getCategoryTree,
  updateCategory,
} from "@/apis/category/category";
import { useCookies } from "react-cookie";
import "./CategoryTree.css";
import Modal from "@/components/Modal";
import * as style from "@/styles/style";

interface CategoryTreeProps {
  onSelect: (category: CategoryTreeResponseDto) => void;
}

const CategoryTree: React.FC<CategoryTreeProps> = ({ onSelect }) => {
  const [cookies] = useCookies(["accessToken"]);

  const [modalStatus, setModalStatus] = useState(false);
  const [updateForm, setUpdateForm] = useState<{
    categoryName: string;
    discountPolicyId: number | null;
  }>({
    categoryName: "",
    discountPolicyId: null,
  });
  const [message, setMessage] = useState("");
  const [categoriesMap, setCategoriesMap] = useState<{
    DOMESTIC?: CategoryTreeResponseDto[];
    FOREIGN?: CategoryTreeResponseDto[];
  }>({});

  const [expandedType, setExpandedType] = useState<
    "DOMESTIC" | "FOREIGN" | null
  >(null);
  const [expandedCategoryIds, setExpandedCategoryIds] = useState<number[]>([]);

  const [categoryType, setCategoryType] = useState<"DOMESTIC" | "FOREIGN">(
    "DOMESTIC"
  );
  const [categoryId, setCategoryId] = useState<number>(0);

  const onUpdateInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setUpdateForm({ ...updateForm, [name]: value });
  };

  const fetchCategories = async (type: "DOMESTIC" | "FOREIGN") => {
    if (!cookies.accessToken) {
      alert("로그인이 필요합니다.");
      return;
    }

    if (!categoriesMap[type]) {
      const res = await getCategoryTree(type, cookies.accessToken);
      if (res.code === "SU") {
        setCategoriesMap((prev) => ({ ...prev, [type]: res.data ?? [] }));
      } else {
        alert("카테고리 조회 실패");
        return;
      }
    }

    setExpandedType((prev) => (prev === type ? null : type));
    setExpandedCategoryIds([]);
  };

  const toggleCategory = (id: number) => {
    setExpandedCategoryIds((prev) =>
      prev.includes(id) ? prev.filter((e) => e !== id) : [...prev, id]
    );
  };

  // * 수정 모달창
  const openUpdateModal = (sub: CategoryTreeResponseDto) => {
    setCategoryId(sub.categoryId);
    setUpdateForm({
      categoryName: sub.categoryName,
      discountPolicyId: sub.discountPolicyId ? sub.discountPolicyId! : null,
    });
    setCategoryType(sub.categoryType);
    setModalStatus(true);
    console.log(modalStatus);
  };

  // * 수정
  const onUpdateCategoryClick = async (categoryId: number) => {
    // setModalMessage("");
    const dto = {
      categoryName: updateForm.categoryName,
      categoryType: categoryType,
      discountPolicyId: updateForm.discountPolicyId!,
    };
    const token = cookies.accessToken;

    if (!token) {
      alert("인증 토큰이 없습니다.");
      return;
    }

    try {
      const updateResponse = await updateCategory(categoryId, dto, token);
      const { code: udpateCode, message } = updateResponse;

      if (udpateCode != "SU") {
        alert(message);
        setMessage(message);
        return;
      }

      alert("수정되었습니다.");
      setModalStatus(false);
      fetchCategories(categoryType);
    } catch (error) {
      console.error(error);
      alert("수정 중 오류가 발생하였습니다.");
    }
  };

  // * 비활성화 / 활성화
  const handleDeactivate = async (sub: CategoryTreeResponseDto) => {
    const confirm = window.confirm(sub.isActive ? "카테고리를 비활성화하시겠습니까?" : "카테고리를 활성화하시겠습니까?");
    if (!confirm) return;

    const token = cookies.accessToken;
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }

    try {
      const res = await deleteCategory(sub.categoryId, token);
      if (res.code != "SU") return alert(res.message);
      alert("카테고리 비활성화 완료");
      fetchCategories(categoryType);
      // onSuccess();
    } catch (error) {
      alert("비활성화 실패");
    }
  };

  const modalContent: React.ReactNode = (
    <>
      <h3>✏️ 카테고리 수정</h3>

      <input
        type="text"
        name="categoryName"
        value={updateForm.categoryName}
        onChange={onUpdateInputChange}
        placeholder={updateForm.categoryName}
        required
      />

      <select
        value={categoryType}
        onChange={(e) =>
          setCategoryType(e.target.value as "DOMESTIC" | "FOREIGN")
        }
      >
        <option value="DOMESTIC">국내도서</option>
        <option value="FOREIGN">해외도서</option>
      </select>

      <input
        type="number"
        name="discountPolicyId"
        value={updateForm.discountPolicyId ?? ""}
        onChange={onUpdateInputChange}
        placeholder={updateForm.discountPolicyId?.toString() ?? ""}
      />
      <button
        onClick={() => onUpdateCategoryClick(categoryId)}
        css={style.createButton}
        style={{ margin: "10px auto", marginRight: 0, marginTop: "auto" }}
      >
        수정
      </button>
    </>
  );

  const renderCategoryTree = (categories?: CategoryTreeResponseDto[]) => {
    if (!categories) return null;

    return categories.map((cat) => {
      const isExpanded = expandedCategoryIds.includes(cat.categoryId);
      const hasChildren = cat.subCategories && cat.subCategories.length > 0;

      return (
        <div key={cat.categoryId}>
          <div
            className="category"
            onClick={() => {
              if (hasChildren) toggleCategory(cat.categoryId);
              onSelect(cat);
            }}
          >
            {hasChildren ? (isExpanded ? "▼" : "▶") : "•"} {cat.categoryName}
          </div>
          {isExpanded && hasChildren && (
            <div>
              {cat.subCategories!.map((sub) => (
                <div
                  key={sub.categoryId}
                  className={`subcategory-item ${sub.isActive ? "" : "inactive"}`}
                  onClick={() => onSelect(sub)}
                >
                  {sub.categoryName}

                  {sub.isActive ? (
                    <>
                      <button onClick={() => openUpdateModal(sub)}>수정</button>
                      <button onClick={() => handleDeactivate(sub)}>
                        비활성화
                      </button>
                    </>
                  ) : (
                    <button onClick={() => handleDeactivate(sub)}>
                      활성화
                    </button>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>
      );
    });
  };

  return (
    <div className="category-container" style={{width: "100%"}}>
      <h2>전체 도서 카테고리</h2>
      <div style={{display: "flex", gap: 16}}>
      <div style={{width: "50%", padding: 10}}>
        <div
          className="category-type"
          onClick={() => fetchCategories("DOMESTIC")}
        >
          {expandedType === "DOMESTIC" ? "▼" : "▶"} 국내 도서
        </div>
        {expandedType === "DOMESTIC" &&
          renderCategoryTree(categoriesMap.DOMESTIC)}
      </div>
      <div style={{width: "50%", padding: 10}}>
        <div
          className="category-type"
          onClick={() => fetchCategories("FOREIGN")}
        >
          {expandedType === "FOREIGN" ? "▼" : "▶"} 해외 도서
        </div>
        {expandedType === "FOREIGN" &&
          renderCategoryTree(categoriesMap.FOREIGN)}
      </div>
        </div>

      {modalStatus && (
        <Modal
          isOpen={modalStatus}
          onClose={() => {
            setModalStatus(false), setMessage("");
          }}
          children={modalContent}
        ></Modal>
      )}
    </div>
  );
};

export default CategoryTree;
