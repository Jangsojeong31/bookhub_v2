package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.stock.request.StockUpdateRequestDto;
import com.bookhub.bookhub_back.dto.stock.response.StockListResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockUpdateResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.StockService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;//재고
    private final StockLogRepository stockLogRepository;//재고 로그 입력
    private final AlertService alertService;//알림
    private final AuthorityRepository authorityRepository;//권한
    private final EmployeeRepository employeeRepository;//직원
    private final BookRepository bookRepository;//책
    private final BranchRepository branchRepository;//지점

    // 재고 수정
    @Override
    @Transactional
    public ResponseDto<StockUpdateResponseDto> updateStock(Long stockId, StockUpdateRequestDto dto) {
        StockActionType actionType = StockActionType.valueOf(dto.getType().toUpperCase());
        Long updatedAmount= null;
        Stock stock = null;

        if (stockId != null) {
            stock = stockRepository.findById(stockId)
                    .orElseThrow(() -> new EntityNotFoundException("해당 재고 ID가 존재하지 않습니다."));
        } else {
            // Book + Branch로 재고 조회
            Book book = bookRepository.findById(dto.getBookIsbn())
                    .orElseThrow(() -> new EntityNotFoundException("도서가 존재하지 않습니다."));

            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new EntityNotFoundException("지점이 존재하지 않습니다."));

            // 재고 조회 -> 없으면 재고 생성
            stock = stockRepository.findByBookIsbnAndBranchId(book, branch)
                    .orElseGet(() -> stockRepository.save(
                            Stock.builder()
                                    .bookIsbn(book)
                                    .branchId(branch)
                                    .bookAmount(0L)
                                    .build()
                    ));
        }

        switch (actionType) {
            case IN -> updatedAmount = stock.getBookAmount() + dto.getAmount();
            case OUT, LOSS -> {
                if (stock.getBookAmount() < dto.getAmount()) {
                    throw new IllegalArgumentException("재고 부족");
                }
                updatedAmount = stock.getBookAmount() - dto.getAmount();
            }
            default -> throw new IllegalArgumentException("잘못된 type");
        }

        stock.setBookAmount(updatedAmount);
        stockRepository.save(stock);

        StockUpdateResponseDto responseDto = StockUpdateResponseDto.builder()
                .stockId(stock.getStockId())
                .bookIsbn(stock.getBookIsbn().getBookIsbn())
                .branchName(stock.getBranchId().getBranchName())
                .type(dto.getType())
                .bookTitle(stock.getBookIsbn().getBookTitle())
                .amount(dto.getAmount())
                .bookAmount(stock.getBookAmount())
                .build();

        // 재고 로그 생성
        StockLog log = StockLog.builder()
                .stockActionType(StockActionType.valueOf(dto.getType()))
                .employeeId(employeeRepository.findById(dto.getEmployeeId()).orElseThrow(()-> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND)))
                .bookIsbn(bookRepository.findById(dto.getBookIsbn()).orElseThrow(()-> new IllegalArgumentException((ResponseMessageKorean.RESOURCE_NOT_FOUND))))
                .branchId(branchRepository.findById(dto.getBranchId()).orElseThrow(()-> new IllegalArgumentException((ResponseMessageKorean.RESOURCE_NOT_FOUND))))
                .amount(dto.getAmount())
                .bookAmount(updatedAmount)
                .description(dto.getDescription())
                .build();

        stockLogRepository.save(log);

        // 알림 기능: 매니저에게 STOCK_LOW 알림
//        if ((actionType == StockActionType.OUT || actionType == StockActionType.LOSS) && updatedAmount <= 5) {
//            Authority managerAuthority = authorityRepository.findByAuthorityName("MANAGER")
//                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));
//
//            final Stock finalStock = stock;
//
//            String alertType = (updatedAmount == 0) ? "STOCK_OUT" : "STOCK_LOW";
//            String message = "[" + stock.getBookIsbn().getBookTitle() + "] 도서의 재고가 "
//                    + (updatedAmount == 0 ? "모두 소진되었습니다." : "부족합니다 (남은 수량: " + updatedAmount + "권)");
//
//            employeeRepository.findAll().stream()
//                    .filter(emp -> emp.getAuthorityId().equals(managerAuthority) && emp.getBranchId().equals(finalStock.getBranchId()))
//                    .forEach(manager -> {
//                        AlertCreateRequestDto alertDto = AlertCreateRequestDto.builder()
//                                .employeeId(manager.getEmployeeId())
//                                .alertType(alertType)
//                                .message(message)
//                                .alertTargetTable("STOCKS")
//                                .targetPk(stock.getStockId())
//                                .targetIsbn(String.valueOf(stock.getBookIsbn()))
//                                .build();
//                        alertService.createAlert(alertDto);
//                    });
//        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 재고 조회
    @Override
    public ResponseDto<List<StockListResponseDto>> searchStocks(String bookTitle, String isbn, String branchName) {
        List<Stock> stocks = stockRepository.searchStocksByConditions(bookTitle, isbn, branchName);

        List<StockListResponseDto> responseDtos = stocks.stream()
                .map(stock -> StockListResponseDto.builder()
                        .stockId(stock.getStockId())
                        .branchId(stock.getBranchId().getBranchId())
                        .bookIsbn(stock.getBookIsbn().getBookIsbn())
                        .branchName(stock.getBranchId().getBranchName())
                        .bookTitle(stock.getBookIsbn().getBookTitle())
                        .amount(stock.getBookAmount())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }
}
