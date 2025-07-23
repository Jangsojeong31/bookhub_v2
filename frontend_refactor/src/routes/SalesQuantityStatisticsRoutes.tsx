import { Route } from "react-router-dom";
import TotalBestSeller from "../views/statistics/bestSeller/BestSellerTotal";
import BestSellerByPeriod from "../views/statistics/bestSeller/BestSellerByPeriod";
import BestSellerByCategory from "../views/statistics/bestSeller/BestSellerByCategory";
import SalesQuantityByPeriod from "../views/statistics/salesQuantity-statistics/SalesQuantityByPeriod";
import SaleQuantityByBranch from "../views/statistics/salesQuantity-statistics/SalesQauntityByBranch";
import SalesQuantityByDiscountPolicy from "../views/statistics/salesQuantity-statistics/SalesQuantityByDiscountPolicy";
import SalesQuantityByCategory from "../views/statistics/salesQuantity-statistics/SalesQuantityByCategory";
import RequireAuth from "@/components/auth/RequireAuth";

function SalesQuantityStatisticsRoutes() {
  return (
    <>
      <Route path="/best-seller" element={<TotalBestSeller />} />
      <Route path="/best-seller/period" element={<BestSellerByPeriod />} />
      <Route path="/best-seller/category" element={<BestSellerByCategory />} />

      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/statistics/sales-quantity/branch" element={<SaleQuantityByBranch />}/>
        <Route path="/statistics/sales-quantity/period" element={<SalesQuantityByPeriod />}/>
        <Route path="/statistics/sales-quantity/discount-policy" element={<SalesQuantityByDiscountPolicy />}/>
        <Route path="/statistics/sales-quantity/category" element={<SalesQuantityByCategory />}/>
      </Route>
    </>
  );
}

export default SalesQuantityStatisticsRoutes;
