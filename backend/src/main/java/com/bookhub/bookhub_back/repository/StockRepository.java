package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Book;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByBookIsbnAndBranchId(Book bookIsbn, Branch branchId);

    @Query("""
            SELECT s FROM Stock s
            WHERE (:bookTitle IS NULL OR s.bookIsbn.bookTitle LIKE CONCAT("%", :bookTitle, "%"))
            AND (:isbn IS NULL OR s.bookIsbn.bookIsbn = :isbn)
            AND (:branchName IS NULL OR s.branchId.branchName LIKE CONCAT("%", :branchName, "%"))
        """)
    List<Stock> searchStocksByConditions(
            @Param("bookTitle") String bookTitle,
            @Param("isbn") String isbn,
            @Param("branchName") String branchName);

    Stock findByBookIsbn_BookIsbnAndBranchId_BranchId(String bookIsbnBookIsbn, Long branchIdBranchId);
}
