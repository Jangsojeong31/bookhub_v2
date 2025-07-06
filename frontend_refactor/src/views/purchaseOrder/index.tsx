import { Route, Routes } from "react-router-dom";
import ElsePurchaseOrder from "./ElsePurchaseOrder";
import ApprovePurchaseOrder from "./purchaseOrderApproval/ApprovePurchaseOrder";
import ElsePurchaseOrderApproval from "./purchaseOrderApproval/ElsePurchaseOrderApproval";
import RequireAuth from "@/components/auth/RequireAuth";

function PurchaseOrder() {
  return (
    <>
      <Route
        path="/purchase-order"
        element={
          <RequireAuth allowedRoles={["ADMIN", "MANAGER"]}>
            <ElsePurchaseOrder />
          </RequireAuth>
        }
      />
      <Route
        path="/purchase-order/approve"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <ApprovePurchaseOrder />
          </RequireAuth>
        }
      />
      <Route
        path="/purchase-order-approval"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <ElsePurchaseOrderApproval />
          </RequireAuth>
        }
      />
    </>
  );
}

export default PurchaseOrder;
