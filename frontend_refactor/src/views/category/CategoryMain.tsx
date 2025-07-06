import { useState } from "react";
import { CategoryTreeResponseDto } from "@/dtos/category/response/category-tree.response.dto";
import { getCategoryTree } from "@/apis/category/category";
import CreateCategory from "./CreateCategory";
import UpdateCategory from "./UpdateCategory";
import CategoryTree from "./CategoryTree";
import { useCookies } from "react-cookie";

type Mode = "create" | "read" | "update" | "delete";

function CategoryMain() {
  const [categories, setCategories] = useState<CategoryTreeResponseDto[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<CategoryTreeResponseDto | null>(null);
  const [mode, setMode] = useState<Mode>("create");
  const [cookies] = useCookies(["accessToken"]);

  const fetchCategories = async () => {
    const token = cookies.accessToken;
    if (!token) {
      alert("로그인이 필요합니다.");
      return;
    }
    
    const res = await getCategoryTree("DOMESTIC", token);
    if (res.code === "SU") {
      setCategories(res.data ?? []);
    } else {
      console.error("카테고리 목록 조회 실패:", res.message);
    }
  };

  const handleSelectCategory = (category: CategoryTreeResponseDto) => {
    setSelectedCategory(category);
  };

  const handleSuccess = () => {
    fetchCategories();
    setSelectedCategory(null);
  };

  const topLevelCategories = categories.filter((cat) => cat.categoryLevel === 1);

  return (
    <div>
    <div className="topBar">
      <div style={{ display: "flex", gap: "12px", marginBottom: "20px" }}>
        <button className="button" onClick={() => setMode("create")}>등록</button>
        <button className="button" onClick={() => setMode("read")}>전체 조회</button>
        <button className="button" onClick={() => setMode("update")}>수정</button>
        <button className="button" onClick={() => setMode("delete")}>삭제</button>
      </div>
      </div>
      <div style={{ display: "flex", gap: "32px" }}>
        {(mode === "read" || mode === "update" || mode === "delete") && (
          <div style={{ flex: 1 }}>
            <CategoryTree onSelect={handleSelectCategory} />
          </div>
        )}

        <div style={{ flex: 1 }}>
          {mode === "create" && (
            <CreateCategory parentCategories={topLevelCategories} onSuccess={fetchCategories} />
          )}

          {mode === "update" && selectedCategory && (
            <UpdateCategory
              category={selectedCategory}
              onSuccess={handleSuccess}
              mode="update"
            />
          )}

          {mode === "delete" && selectedCategory && (
            <UpdateCategory
              category={selectedCategory}
              onSuccess={handleSuccess}
              mode="delete"
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default CategoryMain;
