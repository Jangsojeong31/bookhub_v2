package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockUpdateRequestDto;
import com.bookhub.bookhub_back.dto.stock.response.StockListResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockUpdateResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface StockService {

    ResponseDto<StockUpdateResponseDto> updateStock(Long stockId, @Valid StockUpdateRequestDto dto);

    ResponseDto<List<StockListResponseDto>> searchStocksByBranch(UserPrincipal userPrincipal, String bookTitle, String isbn);

    ResponseDto<List<StockListResponseDto>> searchStocks(String bookTitle, String isbn, String branchName);

}
