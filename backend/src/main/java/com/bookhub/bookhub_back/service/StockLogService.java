package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockLogResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StockLogService {

    ResponseDto<List<StockLogResponseDto>> searchStockLogs(String branchName, String type, String bookIsbn, LocalDate start, LocalDate end);

    ResponseDto<List<StockLogResponseDto>> searchStockLogsByBranch(UserPrincipal userPrincipal, String type, String bookIsbn, LocalDate start, LocalDate end);
}
