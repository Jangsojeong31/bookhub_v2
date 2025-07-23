import { Route, Routes, useNavigate } from "react-router-dom";
import CreateBook from "../views/book/CreateBook";
import UpdateBook from "../views/book/UpdateBook";
import SearchBook from "../views/book/SearchBook";
import BookLogs from "../views/book/book-logs/BookLogs";
import RequireAuth from "@/components/auth/RequireAuth";

function BookRoutes() {
  return (
    <>
      <Route element={<RequireAuth allowedRoles={["ADMIN"]} />}>
        <Route path="/books/create" element={<CreateBook />}/>
        <Route path="/books/edit" element={<UpdateBook />}/>
        <Route path="booklogs" element={<BookLogs />}/>
      </Route>

      <Route path="/books/search" element={<SearchBook />} />
    </>
  );
}
export default BookRoutes;
