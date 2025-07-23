import React from 'react'

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onPrev: () => void;
  onNext: () => void;
}

const Pagination: React.FC<PaginationProps> = ({
  currentPage,
  totalPages,
  onPageChange,
  onPrev,
  onNext
}) => {
  if (totalPages <= 1) return null;

  return (
    <div className='footer'>
      <button
      className='pageBtn'
      onClick={onPrev}
      disabled={currentPage === 0}>
        {"<"}

      </button>
      {Array.from({ length: totalPages }, (_, i) => i).map((i) => (
            <button
              key={i}
              className={`pageBtn${i === currentPage ? " current" : ""}`}
              onClick={() => onPageChange(i)}
            >
              {i + 1}
            </button>
          ))}
          <button
            className="pageBtn"
            onClick={onNext}
            disabled={currentPage >= totalPages - 1}
          >
            {">"}
          </button>
          <span className="pageText">
            {totalPages > 0 ? `${currentPage + 1} / ${totalPages}` : "0 / 0"}
          </span>
    </div>
  )
};

export default Pagination;