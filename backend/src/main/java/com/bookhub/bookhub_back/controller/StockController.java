package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockUpdateRequestDto;
import com.bookhub.bookhub_back.dto.stock.response.StockListResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockUpdateResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    private final String STOCK_ADMIN = ApiMappingPattern.ADMIN + "/stocks";
    private final String STOCK_MANAGER = ApiMappingPattern.MANAGER + "/stocks";

    //책 재고 손실 시 수량 변경(Update)
    @PutMapping(STOCK_MANAGER + "/{stockId}")
    public ResponseEntity<ResponseDto<StockUpdateResponseDto>> updateStock(
            @PathVariable Long stockId,
            @Valid @RequestBody StockUpdateRequestDto dto
    ){
        ResponseDto<StockUpdateResponseDto> responseDto = stockService.updateStock(stockId,dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    // 재고 조회 - manager : 사용자 소속 지점 재고 조회
    @GetMapping(STOCK_MANAGER)
    public ResponseEntity<ResponseDto<List<StockListResponseDto>>> searchStocksByBranch(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) String isbn
    ) {
        ResponseDto<List<StockListResponseDto>> responseDto = stockService.searchStocksByBranch(userPrincipal, bookTitle, isbn);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

    // 재고 조회 - admin : 전체 지점 재고 조회 (책제목, isbn, 지점이름)
    @GetMapping(STOCK_ADMIN)
    public ResponseEntity<ResponseDto<List<StockListResponseDto>>> searchStocks(
            @RequestParam(required = false) String bookTitle,
            @RequestParam(required = false) String isbn,
            @RequestParam(required = false) String branchName
    ) {
        ResponseDto<List<StockListResponseDto>> responseDto = stockService.searchStocks(bookTitle, isbn, branchName);
        return ResponseDto.toResponseEntity(HttpStatus.OK, responseDto);
    }

}
