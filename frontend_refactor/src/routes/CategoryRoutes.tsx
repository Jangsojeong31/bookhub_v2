import { Route } from "react-router-dom";
import CategoryMain from "../views/category/CategoryMain";
import RequireAuth from "@/components/auth/RequireAuth";

function CategoryRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/categories" element={<CategoryMain />}/>
      </Route>
    </>
  );
}

export default CategoryRoutes;
