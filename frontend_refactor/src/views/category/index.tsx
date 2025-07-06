import { Route } from "react-router-dom";
import CategoryMain from "./CategoryMain";
import RequireAuth from "@/components/auth/RequireAuth";

function Category() {
  return (
    <>
      <Route
        path="/categories"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <CategoryMain />
          </RequireAuth>
        }
      />
    </>
  );
}

export default Category;
