package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByAuthorEmail(String authorEmail);

    List<Author> findAllByAuthorName(String authorName);

}
