package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.BookLogType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.response.BookLogResponseDto;
import com.bookhub.bookhub_back.entity.Book;
import com.bookhub.bookhub_back.entity.BookLog;
import com.bookhub.bookhub_back.entity.DiscountPolicy;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.repository.BookLogRepository;
import com.bookhub.bookhub_back.service.BookLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLogServiceImpl implements BookLogService {
    private final BookLogRepository bookLogRepository;

    // 로그 조회
    @Override
    public ResponseDto<List<BookLogResponseDto>> getLogsByBook(String isbn) {
        List<BookLog> logs = bookLogRepository.findByBookIsbn_BookIsbn(isbn);
        List<BookLogResponseDto> result = logs.stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, result);
    }

    // 책 등록 로그 생성
    @Override
    public void logCreate(Book book, Employee employee) {
        saveBookLog(book, employee, BookLogType.CREATE, null, null, null);
    }

    // 가격 변경 로그 생성
    @Override
    public void logPriceChange(Book book, Long oldPrice, Employee employee) {
        saveBookLog(book, employee, BookLogType.PRICE_CHANGE, oldPrice, null, null);
    }

    // 할인율 변경 로그 생성
    @Override
    public void logDiscountChange(Book book, Integer oldRate, DiscountPolicy policy, Employee employee) {
        saveBookLog(book, employee, BookLogType.DISCOUNT_RATE, null, oldRate, policy);
    }

    // 책 상태 변경 로그 생성
    @Override
    public void logStatusChange(Book book, Employee employee) {
        saveBookLog(book, employee, BookLogType.STATUS_CHANGE, null, null, null);
    }

    // 책 hidden 처리 로그 생성
    @Override
    public void logHidden(Book book, Employee employee) {
        saveBookLog(book, employee, BookLogType.HIDDEN, null, null, null);
    }

    // 로그 생성 메서드
    private void saveBookLog(Book book, Employee employee, BookLogType type,
                             Long prevPrice, Integer prevDiscount, DiscountPolicy policy) {

        BookLog log = BookLog.builder()
                .bookIsbn(book)
                .employeeId(employee)
                .bookLogType(type)
                .previousPrice(prevPrice)
                .previousDiscountRate(prevDiscount)
                .policyId(policy)
                .changedAt(LocalDate.now())
                .build();

        bookLogRepository.save(log);
    }

    // responseDto 변환 메서드
    private BookLogResponseDto toDto(BookLog log) {
        return BookLogResponseDto.builder()
                .bookLogId(log.getBookLogId())
                .bookIsbn(log.getBookIsbn().getBookIsbn())
                .bookTitle(log.getBookIsbn().getBookTitle())
                .employeeName(log.getEmployeeId().getName())
                .bookLogType(log.getBookLogType().name())
                .previousPrice(log.getPreviousPrice())
                .previousDiscountRate(log.getPreviousDiscountRate())
                .changedAt(log.getChangedAt())
                .build();
    }
}
