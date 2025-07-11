package com.bookhub.bookhub_back.service.statistics;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.BestSellerDto;

import java.util.List;

public interface BestSellerService {
    ResponseDto<List<BestSellerDto>> getTop100BestSellers();

    ResponseDto<List<BestSellerDto>> getWeeklyBestSellers();

    ResponseDto<List<BestSellerDto>> getMonthlyBestSellers();

    ResponseDto<List<BestSellerDto>> getYearlyBestSellers();

    ResponseDto<List<BestSellerDto>> getBestSellersByCategory(Long categoryId);
}
