import {
  Navigate,
  Outlet,
  Route,
  Routes,
  useLocation,
  useNavigate,
} from "react-router-dom";
import { useEmployeeStore } from "./stores/useEmployeeStore";

// import 'react-datepicker/dist/react-datepicker.css';
import LocationRoutes from "./routes/LocationRoutes";
import BookRoutes from "./routes/BookRoutes";
import BranchRoutes from "./routes/BranchRoutes";
import StockLogRoutes from "./routes/StockLogRoutes";
import AlertRoutes from "./routes/AlertRoutes";
import AuthorRoutes from "./routes/AuthorRoutes";
import AuthRoutes from "./routes/AuthRoutes";
import CategoryRoutes from "./routes/CategoryRoutes";
import EmployeeRoutes from "./routes/EmployeeRoutes";
import MainPageRoutes from "./routes/MainPageRoutes";
import PolicyRoutes from "./routes/PolicyRoutes";
import PublisherRoutes from "./routes/PublisherRoutes";
import PurchaseOrderRoutes from "./routes/PurchaseOrderRoutes";
import ReceptionRoutes from "./routes/ReceptionRoutes";
import RevenueStatisticsRoutes from "./routes/RevenueStatisticsRoutes";
import SalesQuantityStatisticsRoutes from "./routes/SalesQuantityStatisticsRoutes";
import StockRoutes from "./routes/StockRoutes";
import StockStatisticsRoutes from "./routes/StockStatisticsRoutes";
import MainLayouts from "./layouts/MainLayouts";

function App() {
  const isLogin = useEmployeeStore((state) => state.isLogin);

  return (
    <>
      {!isLogin ? (
        <Routes>{AuthRoutes()}</Routes>
      ) : (
        <Routes>
          <Route element={<MainLayouts />}>
            <Route path="/" element={<Navigate to="/main" />} />
            {MainPageRoutes()}
            {AlertRoutes()}
            {AuthorRoutes()}
            {BookRoutes()}
            {BranchRoutes()}
            {CategoryRoutes()}
            {EmployeeRoutes()}
            {LocationRoutes()}
            {PolicyRoutes()}
            {PublisherRoutes()}
            {PurchaseOrderRoutes()}
            {ReceptionRoutes()}
            {StockRoutes()}
            {StockLogRoutes()}
            {RevenueStatisticsRoutes()}
            {StockStatisticsRoutes()}
            {SalesQuantityStatisticsRoutes()}
          </Route>
        </Routes>
      )}
    </>
  );
}

export default App;
