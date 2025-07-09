package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockLogResponseDto;
import com.bookhub.bookhub_back.service.StockLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/stock-logs")
@RequiredArgsConstructor
public class StockLogController {
    private final StockLogService stockLogService;

    // branch 의 재고 로그 조회
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<ResponseDto<List<StockLogResponseDto>>> searchStockLogsByBranch(
            @PathVariable(required = false) Long branchId,
            @RequestParam(required = false) StockActionType type,
            @RequestParam(required = false) String bookIsbn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ){
        ResponseDto<List<StockLogResponseDto>> stockLogs = stockLogService.searchStockLogsByBranch(
                branchId, type, bookIsbn, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(stockLogs);
    }

    // employeeId로 로그 조회
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ResponseDto<List<StockLogResponseDto>>> searchStockLogsByEmployee(
            @PathVariable Long employeeId){
        ResponseDto<List<StockLogResponseDto>> stockLogs = stockLogService.searchStockLogsByEmployee(employeeId);
        return ResponseEntity.status(HttpStatus.OK).body(stockLogs);
    }


}
