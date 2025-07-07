package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    @Query("SELECT bc FROM BookCategory bc WHERE bc.parentCategoryId.categoryId = :parentId AND bc.isActive = true")
    List<BookCategory> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.categoryType = :type AND bc.categoryLevel = :level AND bc.isActive = true")
    List<BookCategory> findByTypeAndLevel(CategoryType type, int i);

    @Query("SELECT bc FROM BookCategory bc WHERE bc.parentCategoryId IS NULL AND bc.isActive = true")
    List<BookCategory> findRootCategories();
}