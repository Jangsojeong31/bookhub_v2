package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findAllByAuthorNameContaining(String authorName);

    boolean existsByAuthorEmail(@NotBlank(message = "저자 이메일은 필수입니다.") @Email(message = "올바른 이메일 형식을 입력해주세요.") String authorEmail);

    boolean existsByAuthorEmailAndAuthorIdNot(String authorEmail, Long authorId);
}
