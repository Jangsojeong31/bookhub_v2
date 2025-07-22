import React, { useMemo, useState } from 'react'

function usePagination<T>(items: T[], itemsPerPage: number = 10) {
  const [currentPage, setCurrentPage] = useState(0);

  const totalPages = useMemo(
    () => Math.ceil(items.length / itemsPerPage),
    [items, itemsPerPage]
  );

  const goToPage = (page: number) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  const goPrev = () => {
    if (currentPage > 0) setCurrentPage(currentPage - 1);
  };
  
  const goNext = () => {
    if (currentPage < totalPages - 1) setCurrentPage(currentPage + 1);
  };

  const pagedItems = useMemo( 
    () => 
      items.slice(
        currentPage * itemsPerPage,
        (currentPage + 1) * itemsPerPage
      ),
      [items, currentPage, itemsPerPage]
  )

  return {
      currentPage,
      totalPages,
      pagedItems,
      goToPage,
      goPrev,
      goNext,
      setCurrentPage,
  };
  
}

export default usePagination;