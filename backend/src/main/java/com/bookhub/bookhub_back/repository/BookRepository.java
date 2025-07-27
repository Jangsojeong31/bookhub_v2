package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.entity.Author;
import com.bookhub.bookhub_back.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    @Query("""
        SELECT b FROM Book b
        WHERE b.bookStatus != com.bookhub.bookhub_back.common.enums.BookStatus.HIDDEN
        AND b.bookIsbn = :isbn
        """)
    Optional<Book> findByIdNotHidden(String isbn);

    @Query("""
    SELECT b FROM Book b
    WHERE
        b.bookStatus != com.bookhub.bookhub_back.common.enums.BookStatus.HIDDEN AND (
            b.bookIsbn = :keyword OR
            LOWER(b.bookTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.authorId.authorName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.publisherId.publisherName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.categoryId.categoryName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(b.categoryId.categoryType) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
""")
    List<Book> findAllByKeywordContaining(@Param("keyword") String keyword);

    @Query("""
    SELECT b FROM Book b
    WHERE b.bookStatus != com.bookhub.bookhub_back.common.enums.BookStatus.HIDDEN 
    AND LOWER(b.bookTitle) LIKE LOWER(CONCAT('%', :bookTitle, '%'))
""")
    List<Book> findAllByBookTitleContaining(@Param("bookTitle") String bookTitle);

    boolean existsByAuthorId_AuthorId(Long authorIdAuthorId);

    boolean existsByPublisherId_PublisherId(Long publisherIdPublisherId);

    boolean existsByCategoryId_CategoryId(Long categoryIdCategoryId);

    boolean existsByBookIsbn(String bookIsbn);
}
