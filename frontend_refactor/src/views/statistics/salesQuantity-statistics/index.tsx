import React from "react";
import { Route } from "react-router-dom";
import TotalBestSeller from "./BestSellerTotal";
import BestSellerByPeriod from "./BestSellerByPeriod";
import BestSellerByCategory from "./BestSellerByCategory";
import SalesQuantityByPeriod from "./SalesQuantityByPeriod";
import SaleQuantityByBranch from "./SalesQauntityByBranch";
import SalesQuantityByDiscountPolicy from "./SalesQuantityByDiscountPolicy";
import SalesQuantityByCategory from "./SalesQuantityByCategory";
import RequireAuth from "@/components/auth/RequireAuth";

function SalesQuantityStatistics() {
  return (
    <>
      <Route path="/best-seller" element={<TotalBestSeller />} />
      <Route path="/best-seller/period" element={<BestSellerByPeriod />} />
      <Route path="/best-seller/category" element={<BestSellerByCategory />} />

      <Route
        path="/statistics/sales-quantity/branch"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <SaleQuantityByBranch />
          </RequireAuth>
        }
      />
      <Route
        path="/statistics/sales-quantity/period"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <SalesQuantityByPeriod />
          </RequireAuth>
        }
      />
      <Route
        path="/statistics/sales-quantity/discount-policy"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <SalesQuantityByDiscountPolicy />
          </RequireAuth>
        }
      />
      <Route
        path="/statistics/sales-quantity/category"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <SalesQuantityByCategory />
          </RequireAuth>
        }
      />
    </>
  );
}

export default SalesQuantityStatistics;
