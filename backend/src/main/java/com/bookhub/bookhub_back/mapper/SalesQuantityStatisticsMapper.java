package com.bookhub.bookhub_back.mapper;

import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.CategorySalesQuantityDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.SalesQuantityStatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SalesQuantityStatisticsMapper {
    List<SalesQuantityStatisticsDto> findDailySalesQuantity(@Param("month") int month);

    List<SalesQuantityStatisticsDto> findWeeklySalesQuantity(@Param("year") int year, @Param("month") int month);

    List<SalesQuantityStatisticsDto> findMonthlySalesQuantity(@Param("year") int year);

    List<SalesQuantityStatisticsDto> findSalesQuantityByDiscountPolicy(@Param("year") int year, @Param("quarter") int quarter);

    List<SalesQuantityStatisticsDto> findSalesQuantityByBranch(@Param("year") int year, @Param("month") int month);

    List<CategorySalesQuantityDto> findSalesQuantityByCategory();
}
