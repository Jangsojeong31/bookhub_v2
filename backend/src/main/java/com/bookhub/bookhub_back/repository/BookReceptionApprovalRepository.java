package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.BookReceptionApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReceptionApprovalRepository extends JpaRepository<BookReceptionApproval, Long> {

}
