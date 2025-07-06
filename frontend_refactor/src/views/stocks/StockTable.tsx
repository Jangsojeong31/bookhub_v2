import { Stock } from "./stock";


interface StockTableProps {
  stocks: Stock[];
  onEdit: (stock: Stock) => void;
}

function StockTable({ stocks, onEdit }: StockTableProps) {
  return (
    <table className="table-auto w-full border">
      <thead>
        <tr>
          <th className="border p-2">도서 제목</th>
          <th className="border p-2">지점명</th>
          <th className="border p-2">수량</th>
          <th className="border p-2">수정</th>
        </tr>
      </thead>
      <tbody>
        {stocks.map((stock) => (
          <tr key={stock.stockId}>
            <td className="border p-2">{stock.bookTitle}</td>
            <td className="border p-2">{stock.branchName}</td>
            <td className="border p-2">{stock.amount}</td>
            <td className="border p-2">
              <button onClick={() => onEdit(stock)} className="btn">수정</button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default StockTable;
