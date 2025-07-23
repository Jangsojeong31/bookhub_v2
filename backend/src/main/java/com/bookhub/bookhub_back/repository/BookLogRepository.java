package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.BookLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookLogRepository extends JpaRepository<BookLog, Long> {

    List<BookLog> findByBookIsbn_BookIsbn(String bookIsbn);
}
