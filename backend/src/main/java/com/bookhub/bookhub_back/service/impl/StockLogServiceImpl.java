package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockLogResponseDto;
import com.bookhub.bookhub_back.entity.Book;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.StockLog;
import com.bookhub.bookhub_back.repository.BookRepository;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.repository.EmployeeRepository;
import com.bookhub.bookhub_back.repository.StockLogRepository;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.StockLogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockLogServiceImpl implements StockLogService {
    private final StockLogRepository stockLogRepository;
    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public ResponseDto<List<StockLogResponseDto>> searchStockLogs(
            String branchName, String type, String bookIsbn, LocalDate start, LocalDate end
    ) {
        StockActionType enumType = null;
        if (type != null && !type.isBlank()) {
            enumType = StockActionType.valueOf(type.trim().toUpperCase());
        }

        List<StockLog> logs = stockLogRepository.searchStockLogsByConditions(branchName, enumType, bookIsbn, start, end);

        List<StockLogResponseDto> responseDtos = logs.stream()
                .map(stockLog -> StockLogResponseDto.builder()
                        .stockLogId(stockLog.getLogId())
                        .type(String.valueOf(stockLog.getStockActionType()))
                        .employeeName(stockLog.getEmployeeId().getName())
                        .bookTitle(stockLog.getBookIsbn().getBookTitle())
                        .branchName(stockLog.getBranchId().getBranchName())
                        .amount(stockLog.getAmount())
                        .actionDate(stockLog.getActionedAt())
                        .build())
                .collect(Collectors.toList());
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<StockLogResponseDto>> searchStockLogsByBranch(
            UserPrincipal userPrincipal, String type, String bookIsbn, LocalDate start, LocalDate end
    ) {
        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);
        String branchName = branch.getBranchName();

        StockActionType enumType = null;
        if (type != null && !type.isBlank()) {
            enumType = StockActionType.valueOf(type.trim().toUpperCase());
        }

        List<StockLog> logs = stockLogRepository.searchStockLogsByConditions(branchName, enumType, bookIsbn, start, end);

        List<StockLogResponseDto> responseDtos = logs.stream()
                .map(stockLog -> StockLogResponseDto.builder()
                        .stockLogId(stockLog.getLogId())
                        .type(String.valueOf(stockLog.getStockActionType()))
                        .employeeName(stockLog.getEmployeeId().getName())
                        .bookTitle(stockLog.getBookIsbn().getBookTitle())
                        .branchName(stockLog.getBranchId().getBranchName())
                        .amount(stockLog.getAmount())
                        .actionDate(stockLog.getActionedAt())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

}
