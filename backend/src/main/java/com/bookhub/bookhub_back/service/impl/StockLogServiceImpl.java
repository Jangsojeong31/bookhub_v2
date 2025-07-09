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
import com.bookhub.bookhub_back.service.StockLogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public ResponseDto<List<StockLogResponseDto>> searchStockLogsByBranch(
            Long branchId, StockActionType type, String bookIsbn, LocalDateTime start, LocalDateTime end
    ) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(EntityNotFoundException::new);

        List<StockLog> logs = stockLogRepository.searchStockLogsByConditions(branch, type, bookIsbn, start, end);

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
    public ResponseDto<List<StockLogResponseDto>> searchStockLogsByEmployee(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        List<StockLog> logs = stockLogRepository.findByEmployeeId(employee);

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
