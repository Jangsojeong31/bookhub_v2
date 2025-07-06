package com.bookhub.bookhub_back.repository;

import com.bookhub.bookhub_back.common.enums.CategoryType;
import com.bookhub.bookhub_back.dto.category.response.CategoryResponseDto;
import com.bookhub.bookhub_back.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    List<BookCategory> findAllByParentCategoryIdIsNull();

    List<BookCategory> findAllByParentCategoryId(Long parentCategoryId);

    List<BookCategory> findByCategoryTypeAndCategoryLevel(CategoryType categoryType, int categoryLevel);
}