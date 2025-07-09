package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StockLogService {

    ResponseDto<List<StockLogResponseDto>> searchStockLogsByBranch(Long branchId, StockActionType type, String bookIsbn, LocalDateTime start, LocalDateTime end);

    ResponseDto<List<StockLogResponseDto>> searchStockLogsByEmployee(Long employeeId);
}
