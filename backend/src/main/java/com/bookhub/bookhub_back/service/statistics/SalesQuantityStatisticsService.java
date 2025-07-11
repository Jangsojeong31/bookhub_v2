package com.bookhub.bookhub_back.service.statistics;


import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.CategorySalesQuantityDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.SalesQuantityStatisticsDto;

import java.util.List;

public interface SalesQuantityStatisticsService {

    ResponseDto<List<SalesQuantityStatisticsDto>> getDailySalesQuantity(int month);

    ResponseDto<List<SalesQuantityStatisticsDto>> getWeeklySalesQuantity(int year, int month);

    ResponseDto<List<SalesQuantityStatisticsDto>> getMonthlySalesQuantity(int year);

    ResponseDto<List<CategorySalesQuantityDto>> getSalesQuantityByCategory();

    ResponseDto<List<SalesQuantityStatisticsDto>> getSalesQuantityByDiscountPolicy(int year, int quarter);

    ResponseDto<List<SalesQuantityStatisticsDto>> getSalesQuantityByBranch(int year, int month);
}
