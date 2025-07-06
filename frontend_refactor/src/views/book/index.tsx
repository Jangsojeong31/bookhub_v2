import { Route, Routes, useNavigate } from "react-router-dom";
import CreateBook from "./CreateBook";
import UpdateBook from "./UpdateBook";
import SearchBook from "./SearchBook";
import BookLogs from "./book-logs/BookLogs";
import RequireAuth from "@/components/auth/RequireAuth";

function Book() {
  const navigate = useNavigate();
  return (
    <Routes>
      <Route
        path="create"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <CreateBook onSuccess={async () => navigate("/books/search")} />
          </RequireAuth>
        }
      />
      <Route
        path="edit"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <UpdateBook />
          </RequireAuth>
        }
      />
      <Route path="search" element={<SearchBook />} />
      <Route
        path="booklogs"
        element={
          <RequireAuth allowedRoles={["ADMIN"]}>
            <BookLogs />
          </RequireAuth>
        }
      />
    </Routes>
  );
}
export default Book;
