package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockLogResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.StockLogService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class StockLogController {
    private final StockLogService stockLogService;

    private static final String STOCK_LOGS_ADMIN = ApiMappingPattern.ADMIN + "/stock-logs";
    private static final String STOCK_LOGS_MANAGER = ApiMappingPattern.MANAGER + "/stock-logs";

    // 재고 로그 조회 - admin
    @GetMapping(STOCK_LOGS_ADMIN)
    @Operation(summary = "재고 로그 조건별 조회 - admin")
    public ResponseEntity<ResponseDto<List<StockLogResponseDto>>> searchStockLogs(
            @RequestParam(required = false) String branchName,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String bookIsbn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ){
        ResponseDto<List<StockLogResponseDto>> stockLogs = stockLogService.searchStockLogs(
                branchName, type, bookIsbn, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(stockLogs);
    }

    // 재고 로그 조회 (소속 지점만) - manager
    @GetMapping(STOCK_LOGS_MANAGER)
    @Operation(summary = "재고 로그 조건별 조회 (소속 지점) - manager")
    public ResponseEntity<ResponseDto<List<StockLogResponseDto>>> searchStockLogsByBranch(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String bookIsbn,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ){
        ResponseDto<List<StockLogResponseDto>> stockLogs = stockLogService.searchStockLogsByBranch(
                userPrincipal, type, bookIsbn, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(stockLogs);
    }


}
