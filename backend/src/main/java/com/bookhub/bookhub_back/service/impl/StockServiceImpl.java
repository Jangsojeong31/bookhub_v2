package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.stock.request.StockRequestDto;
import com.bookhub.bookhub_back.dto.stock.response.StockListResponseDto;
import com.bookhub.bookhub_back.dto.stock.response.StockUpdateResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.security.UserPrincipal;
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
    private final StockRepository stockRepository;
    private final StockLogRepository stockLogRepository;
    private final AlertService alertService;
    private final AuthorityRepository authorityRepository;
    private final EmployeeRepository employeeRepository;
    private final BookRepository bookRepository;
    private final BranchRepository branchRepository;

    // 재고 생성
    @Override
    @Transactional
    public ResponseDto<Void> createStock(StockRequestDto dto) {
        Book book = bookRepository.findByIdNotHidden(dto.getBookIsbn())
                .orElseThrow(EntityNotFoundException::new);

        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        Stock stock = Stock.builder()
                .bookIsbn(book)
                .branchId(branch)
                .bookAmount(dto.getAmount())
                .build();

        stockRepository.save(stock);

        // 재고 로그 생성
        StockLog log = StockLog.builder()
                .stockActionType(StockActionType.valueOf(dto.getType()))
                .employeeId(employeeRepository.findById(dto.getEmployeeId()).orElseThrow(EntityNotFoundException::new))
                .bookIsbn(bookRepository.findByIdNotHidden(dto.getBookIsbn()).orElseThrow(EntityNotFoundException::new))
                .branchId(branchRepository.findById(dto.getBranchId()).orElseThrow(EntityNotFoundException::new))
                .amount(dto.getAmount())
                .bookAmount(dto.getAmount())
                .description(dto.getDescription())
                .build();

        stockLogRepository.save(log);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 재고량 수정
    @Override
    @Transactional
    public ResponseDto<StockUpdateResponseDto> updateStock(Long stockId, StockRequestDto dto) {
        StockActionType actionType = StockActionType.valueOf(dto.getType().toUpperCase());
        Long updatedAmount= null;

        Stock stock = stockRepository.findById(stockId)
                    .orElseThrow(EntityNotFoundException::new);

        switch (actionType) {
            case IN -> updatedAmount = stock.getBookAmount() + dto.getAmount();
            case OUT, LOSS -> {
                if (stock.getBookAmount() < dto.getAmount()) {
                    throw new IllegalArgumentException("잘못된 입력값입니다. (재고 부족)");
                }
                updatedAmount = stock.getBookAmount() - dto.getAmount();
            }
            default -> throw new IllegalArgumentException();
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
                .employeeId(employeeRepository.findById(dto.getEmployeeId()).orElseThrow(EntityNotFoundException::new))
                .bookIsbn(bookRepository.findByIdNotHidden(dto.getBookIsbn()).orElseThrow(EntityNotFoundException::new))
                .branchId(branchRepository.findById(dto.getBranchId()).orElseThrow(EntityNotFoundException::new))
                .amount(dto.getAmount())
                .bookAmount(updatedAmount)
                .description(dto.getDescription())
                .build();

        stockLogRepository.save(log);

        // 알림 기능: 매니저에게 STOCK_LOW 알림
        if ((actionType == StockActionType.OUT || actionType == StockActionType.LOSS) && updatedAmount <= 5) {

            Authority managerAuthority = authorityRepository.findByAuthorityName("MANAGER")
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessageKorean.USER_NOT_FOUND));

            final Stock finalStock = stock;

            String alertType = (updatedAmount == 0) ? "STOCK_OUT" : "STOCK_LOW";
            String message = "[" + stock.getBookIsbn().getBookTitle() + "] 도서의 재고가 "
                    + (updatedAmount == 0 ? "모두 소진되었습니다." : "부족합니다 (남은 수량: " + updatedAmount + "권)");

            employeeRepository.findAllByPositionId_AuthorityAndBranchId_BranchId(managerAuthority, finalStock.getBranchId().getBranchId())
                    .forEach(manager -> {
                        AlertCreateRequestDto alertDto = AlertCreateRequestDto.builder()
                                .employeeId(manager.getEmployeeId())
                                .alertType(alertType)
                                .message(message)
                                .alertTargetTable("STOCKS")
                                .targetPk(finalStock.getStockId())
                                .targetIsbn(String.valueOf(finalStock.getBookIsbn()))
                                .build();
                        alertService.createAlert(alertDto);
                    });
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // manager 재고 조회 (지점별)
    @Override
    public ResponseDto<List<StockListResponseDto>> searchStocksByBranch(UserPrincipal userPrincipal, String bookTitle, String isbn) {
        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        String branchName = branch.getBranchName();

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

    // admin 재고 조회
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
