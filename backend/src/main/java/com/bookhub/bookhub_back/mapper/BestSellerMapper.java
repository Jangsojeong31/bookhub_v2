package com.bookhub.bookhub_back.mapper;

import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.BestSellerDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface BestSellerMapper {
    List<BestSellerDto> findTop100BestSellers();

    List<BestSellerDto> findWeeklyBestSellers();

    List<BestSellerDto> findMonthlyBestSellers();

    List<BestSellerDto> findYearlyBestSellers();

    List<BestSellerDto> findBestSellersByCategory(@Param("categoryId") Long categoryId);
}
