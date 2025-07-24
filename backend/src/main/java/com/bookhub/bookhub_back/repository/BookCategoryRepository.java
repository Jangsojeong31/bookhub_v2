package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.entity.BookCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    @Query("SELECT bc FROM BookCategory bc WHERE bc.parentCategoryId.categoryId = :parentId")
    List<BookCategory> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.categoryType = :type AND bc.categoryLevel = :level")
    List<BookCategory> findByTypeAndLevel(CategoryType type, int level);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.parentCategoryId IS NULL")
    List<BookCategory> findRootCategories();

    boolean existsByCategoryNameAndCategoryTypeAndParentCategoryId_CategoryId(String categoryName, CategoryType categoryType, Long parentCategoryIdCategoryId);

    boolean existsByCategoryNameAndCategoryTypeAndParentCategoryId_CategoryIdAndCategoryIdNot(String categoryName, CategoryType categoryType, Long parentCategoryIdCategoryId, Long categoryId);

    boolean existsByCategoryNameAndCategoryTypeAndParentCategoryIdIsNullAndCategoryIdNot(String categoryName, CategoryType categoryType, Long categoryId);

    boolean existsByCategoryNameAndCategoryTypeAndParentCategoryIdIsNull(@NotBlank(message = "카테고리 이름은 필수입니다.") String categoryName, @NotNull(message = "카테고리 타입은 필수입니다.") CategoryType categoryType);
}