package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.DisplayType;
import com.bookhub.bookhub_back.entity.Book;
import com.bookhub.bookhub_back.entity.BookDisplayLocation;
import com.bookhub.bookhub_back.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDisplayLocationRepository extends JpaRepository<BookDisplayLocation, Long> {

    @Query("SELECT l FROM BookDisplayLocation l WHERE l.branchId = :branch AND l.bookIsbn IN :books")
    List<BookDisplayLocation> findByBranchAndBooks(@Param("branch") Branch branch, @Param("books") List<Book> books);

    boolean existsByBookIsbn_BookIsbnAndFloorAndHallAndSectionAndDisplayType(String bookIsbnBookIsbn, String floor, String hall, String section, DisplayType displayType);
}
