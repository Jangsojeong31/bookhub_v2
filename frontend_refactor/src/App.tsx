import Sidebar from "./layouts/sidebar";
import Header from "./layouts/header";
import {
  Navigate,
  Outlet,
  Route,
  Routes,
  useLocation,
  useNavigate,
} from "react-router-dom";
import { useCookies } from "react-cookie";
import { useEmployeeStore } from "./stores/employee.store";
import { useEffect, useRef, useState } from "react";
import Main from "./views/main";
import Alert from "./views/alert";
import Auth from "./views/auth";
import Author from "./views/author";
import Book from "./views/book";
import Category from "./views/category";
import Employee from "./views/employee";
import Policy from "./views/policy";
import Publisher from "./views/publisher";
import PurchaseOrder from "./views/purchaseOrder";
import Reception from "./views/reception";

// import 'react-datepicker/dist/react-datepicker.css';
import SalesQuantityStatistics from "./views/statistics/salesQuantity-statistics";
import StockLog from "./views/stock-logs";
import Stock from "./views/stocks";
import Revenue from "./views/statistics/revenue";

import StockStatistics from "./views/statistics/stockstatistics";

import Branch from "./views/branch";
import AlertPage from "./views/alert/AlertPage";
import LocationPage from "./views/location/LocationPage";
import BookLogs from "./views/book/book-logs/BookLogs";
import RequireAuth from "./components/auth/RequireAuth";

function App() {
  const isLogin = useEmployeeStore((state) => state.isLogin);

  if (!isLogin) {
    return <Routes>{Auth()}</Routes>;
  }

  return (
    <>
      <div
        style={{
          height: "100vh",
          display: "flex",
          flexDirection: "column",
          border: "none",
        }}
      >
        <Header />
        <div style={{ flex: 1, display: "flex", minHeight: 0 }}>
          <Sidebar />
          <main
            style={{
              flex: 1,
              padding: "30px",
              minWidth: "1500px",
            }}
          >
            <Routes>
              <Route path="/" element={<Navigate to="/main" />} />
              {/* <Route path="/publishers" element={<Publisher />} /> */}
              <Route path="/alerts/*" element={<AlertPage />} />
              <Route path="/books/*" element={<Book />} />
              <Route path="/booklogs/*" element={<BookLogs />} />

              <Route path="/publishers/*" element={<Publisher />} />
              <Route path="/policies/*" element={<Policy />} />
              <Route path="/branch/locations" element={<LocationPage />} />
              <Route
                path="/stock-logs/*"
                element={
                  <RequireAuth allowedRoles={["ADMIN"]}>
                    <StockLog />
                  </RequireAuth>
                }
              />
              <Route path="/stocks/*" element={<Stock />} />
              <Route
                path="/statistics/revenue/*"
                element={
                  <RequireAuth allowedRoles={["ADMIN"]}>
                    <Revenue />
                  </RequireAuth>
                }
              />
              <Route
                path="/statistics/stocks/*"
                element={
                  <RequireAuth allowedRoles={["ADMIN"]}>
                    <StockStatistics />
                  </RequireAuth>
                }
              />
              {Main()}
              {/* {Alert()} */}

              {/* {Publisher()} */}

              {/*Book()*/}
              {Branch()}
              {/* {BookLocation() */}
              {Category()}
              {Reception()}
              {/*Policy()}
              {Publisher()}
              {StockStatistics()}
              {Stocks()} */}
              {Author()}
              {PurchaseOrder()}
              {SalesQuantityStatistics()}
              {Employee()}
            </Routes>
          </main>
        </div>
      </div>
    </>
  );
}

export default App;
