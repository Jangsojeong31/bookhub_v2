import { Route, Routes } from "react-router-dom";
import ElsePurchaseOrder from "../views/purchaseOrder/ElsePurchaseOrder";
import ApprovePurchaseOrder from "../views/purchaseOrder/purchaseOrderApproval/ApprovePurchaseOrder";
import ElsePurchaseOrderApproval from "../views/purchaseOrder/purchaseOrderApproval/ElsePurchaseOrderApproval";
import RequireAuth from "@/components/auth/RequireAuth";

function PurchaseOrderRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/purchase-order" element={<ElsePurchaseOrder />}/>
        <Route path="/purchase-order/approve" element={<ApprovePurchaseOrder />}/>
        <Route path="/purchase-order-approval" element={<ElsePurchaseOrderApproval />}/>
      </Route>
    </>
  );
}

export default PurchaseOrderRoutes;
