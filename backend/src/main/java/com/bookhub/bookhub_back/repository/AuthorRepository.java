package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

}
