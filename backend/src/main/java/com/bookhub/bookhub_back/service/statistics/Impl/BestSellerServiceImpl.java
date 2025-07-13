package com.bookhub.bookhub_back.service.statistics.Impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.salesQuantity.BestSellerDto;
import com.bookhub.bookhub_back.mapper.BestSellerMapper;
import com.bookhub.bookhub_back.service.statistics.BestSellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BestSellerServiceImpl implements BestSellerService {
    private final BestSellerMapper bestSellerMapper;

    @Override
    public ResponseDto<List<BestSellerDto>> getTop100BestSellers() {
        List<BestSellerDto> responseDtos = bestSellerMapper.findTop100BestSellers();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<BestSellerDto>> getWeeklyBestSellers() {
        List<BestSellerDto> responseDtos = bestSellerMapper.findWeeklyBestSellers();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<BestSellerDto>> getMonthlyBestSellers() {
        List<BestSellerDto> responseDtos = bestSellerMapper.findMonthlyBestSellers();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<BestSellerDto>> getYearlyBestSellers() {
        List<BestSellerDto> responseDtos = bestSellerMapper.findYearlyBestSellers();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<BestSellerDto>> getBestSellersByCategory(Long categoryId) {
        List<BestSellerDto> responseDtos = bestSellerMapper.findBestSellersByCategory(categoryId);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }
}
