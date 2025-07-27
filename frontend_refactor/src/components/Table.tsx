/** @jsxImportSource @emotion/react */
import { css, SerializedStyles } from "@emotion/react";

interface Column<T> {
  header: string;
  accessor: keyof T | string;
  cell?: (item: T, index: number) => React.ReactNode;
}

interface Action<T> {
  label: string;
  onClick: (item: T) => void;
  buttonCss?: SerializedStyles;
}

interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  actions?: Action<T>[];
}

function DataTable<T extends { [key: string]: any }> ({
  columns,
  data,
  actions = [],
}: DataTableProps<T>) {
  return (
    <table>
      <thead>
        <tr>
          {columns.map((col, index) => (
            <th key={index}>{col.header}</th>
          ))}
          {actions.length > 0 && <th>작업</th>}
        </tr>
      </thead>
      <tbody>
        {data.map((item, rowIndex) => (
          <tr key={rowIndex}>
            {columns.map((col, colIndex) => (
              <td key={colIndex}>
                {col.cell 
                  ? col.cell(item, rowIndex) 
                  : item[col.accessor as keyof T] ?? "-"}
              </td>
            ))}
            {actions.length > 0 && (
              <td>
                {actions.map((action, i) => (
                  <button
                  key={i}
                  onClick={() => action.onClick(item)}
                  css={action.buttonCss}
                  >
                    {action.label}
                  </button>
                ))}
              </td>
            )}
          </tr>
        ))}
      </tbody>
    </table>
  );
}

export default DataTable;