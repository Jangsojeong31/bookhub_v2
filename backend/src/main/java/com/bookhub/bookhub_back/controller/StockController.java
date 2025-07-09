package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockUpdateRequestDto;
import com.bookhub.bookhub_back.dto.stock.response.StockListResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockUpdateResponseDto;
import com.bookhub.bookhub_back.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 +ApiMappingPattern.MANAGER+"/stocks")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    //책 재고 손실 시 수량 변경(Update)
    @PutMapping("/{stockId}")
    public ResponseEntity<ResponseDto<StockUpdateResponseDto>> updateStock(
            @PathVariable Long stockId,
            @Valid @RequestBody StockUpdateRequestDto dto
    ){
        ResponseDto<StockUpdateResponseDto> responseDto = stockService.updateStock(stockId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 재고 조회 (책제목, isbn, 지점이름)
    @GetMapping
    public ResponseEntity<ResponseDto<List<StockListResponseDto>>> searchStocks(
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String branchName
    ) {
        ResponseDto<List<StockListResponseDto>> responseDto = stockService.searchStocks(bookTitle, isbn, branchName);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

}
