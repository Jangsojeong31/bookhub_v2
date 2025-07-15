package com.bookhub.bookhub_back.service.statistics.Impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.CategorySalesQuantityDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.SalesQuantityStatisticsDto;
import com.bookhub.bookhub_back.mapper.SalesQuantityStatisticsMapper;
import com.bookhub.bookhub_back.service.statistics.SalesQuantityStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesQuantityStatisticsServiceImpl implements SalesQuantityStatisticsService {
    private final SalesQuantityStatisticsMapper statisticsMapper;

    @Override
    public ResponseDto<List<SalesQuantityStatisticsDto>> getDailySalesQuantity(int month) {
        List<SalesQuantityStatisticsDto>  responseDtos = statisticsMapper.findDailySalesQuantity(month);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<SalesQuantityStatisticsDto>> getWeeklySalesQuantity(int year, int month) {
        List<SalesQuantityStatisticsDto>  responseDtos = statisticsMapper.findWeeklySalesQuantity(year, month);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<SalesQuantityStatisticsDto>> getMonthlySalesQuantity(int year) {
        List<SalesQuantityStatisticsDto>  responseDtos = statisticsMapper.findMonthlySalesQuantity(year);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<CategorySalesQuantityDto>> getSalesQuantityByCategory() {
        List<CategorySalesQuantityDto>  responseDtos = statisticsMapper.findSalesQuantityByCategory();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<SalesQuantityStatisticsDto>> getSalesQuantityByDiscountPolicy(int year, int quarter) {
        List<SalesQuantityStatisticsDto>  responseDtos = statisticsMapper.findSalesQuantityByDiscountPolicy(year, quarter);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<SalesQuantityStatisticsDto>> getSalesQuantityByBranch(int year, int month) {
        List<SalesQuantityStatisticsDto>  responseDtos = statisticsMapper.findSalesQuantityByBranch(year, month);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }
}
