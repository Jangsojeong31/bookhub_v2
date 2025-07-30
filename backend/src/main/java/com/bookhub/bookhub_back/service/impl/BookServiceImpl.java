package com.bookhub.bookhub_back.service.impl;
import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.BookStatus;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookCreateRequestDto;
import com.bookhub.bookhub_back.dto.book.request.BookUpdateRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.BookLogService;
import com.bookhub.bookhub_back.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final DiscountPolicyRepository discountPolicyRepository;
    private final BookCategoryRepository categoryRepository;
    private final BookLogService bookLogService;
    private final EmployeeRepository employeeRepository;
    private final UploadFileRepository uploadFileRepository;

    @Value("${file.upload.path}")
    private String uploadDir;

    // 도서 등록
    @Override
    @Transactional
    public ResponseDto<BookResponseDto> createBook(BookCreateRequestDto dto, UserPrincipal userPrincipal, MultipartFile coverImageFile) throws IOException {
        if(bookRepository.existsByBookIsbn(dto.getIsbn())) {
            throw new DuplicateEntityException("이미 존재하는 ISBN 입니다.");
        }

        Employee employee = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        BookCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(EntityNotFoundException::new);

        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(EntityNotFoundException::new);

        DiscountPolicy policy = dto.getPolicyId() != null
                ? discountPolicyRepository.findById(dto.getPolicyId())
                    .orElseThrow(EntityNotFoundException::new)
                : null;

        UploadFile coverImage = coverImageFile != null && !coverImageFile.isEmpty()
                ? saveAndRecordCoverImage(coverImageFile, dto.getIsbn())
                : null;

        Book book = Book.builder()
                .bookIsbn(dto.getIsbn())
                .categoryId(category)
                .authorId(author)
                .publisherId(publisher)
                .bookTitle(dto.getBookTitle())
                .bookPrice(dto.getBookPrice())
                .publishedDate(dto.getPublishedDate())
                .coverImage(coverImage)
                .pageCount(dto.getPageCount())
                .language(dto.getLanguage())
                .description(dto.getDescription())
                .bookStatus(BookStatus.ACTIVE)
                .discountPolicyId(policy)
                .build();

        Book savedBook = bookRepository.save(book);

        BookResponseDto responseDto = toResponseDto(savedBook);

        // 책 등록 로그
        bookLogService.logCreate(savedBook, employee);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 도서 검색
    @Override
    public ResponseDto<List<BookResponseDto>> searchBook(String keyword) {
        List<Book> books = bookRepository.findAllByKeywordContaining(keyword);

        List<BookResponseDto> responseDtos = books.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 도서 수정
    @Override
    @Transactional
    public ResponseDto<BookResponseDto> updateBook(String isbn, BookUpdateRequestDto dto, UserPrincipal userPrincipal, MultipartFile coverImageFile) throws IOException {
        Employee employee = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        Book book = bookRepository.findByIdNotHidden(isbn)
                .orElseThrow(EntityNotFoundException::new);

        BookCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        DiscountPolicy policy = dto.getPolicyId() != null
                ? discountPolicyRepository.findById(dto.getPolicyId())
                .orElseThrow(EntityNotFoundException::new)
                : null;

        UploadFile coverImage = coverImageFile != null && !coverImageFile.isEmpty()
                ? saveAndRecordCoverImage(coverImageFile, dto.getIsbn())
                : null;

        // 로그 기록용 데이터
        Long oldPrice = book.getBookPrice();
        DiscountPolicy oldPolicy = book.getDiscountPolicyId();
        BookStatus oldStatus = book.getBookStatus();
        Integer oldRate = oldPolicy != null ? oldPolicy.getDiscountPercent() : null;

        // 도서 수정
        book.setBookPrice(dto.getBookPrice());
        book.setCategoryId(category);
        book.setDescription(dto.getDescription());
        book.setBookStatus(BookStatus.valueOf(dto.getBookStatus().toUpperCase()));
        book.setDiscountPolicyId(policy);
        if (coverImage != null) book.setCoverImage(coverImage);

        Book updatedBook = bookRepository.save(book);

        BookResponseDto responseDto = toResponseDto(updatedBook);

         // 로그 기록
        if (!oldPrice.equals(dto.getBookPrice())) {
            bookLogService.logPriceChange(updatedBook, oldPrice, employee);
        }

        DiscountPolicy newPolicy = updatedBook.getDiscountPolicyId();
        Integer newRate = newPolicy != null ? newPolicy.getDiscountPercent() : null;
        if ((oldRate != null && !oldRate.equals(newRate)) || (oldRate == null && newRate != null)) {
            bookLogService.logDiscountChange(updatedBook, oldRate != null ? oldRate : 0, newPolicy, employee);
        }

        if (oldStatus != BookStatus.valueOf(dto.getBookStatus().toUpperCase())) {
            bookLogService.logStatusChange(updatedBook, employee);
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 도서 삭제(hidden 처리)
    @Override
    @Transactional
    public ResponseDto<Void> hideBook(String isbn, UserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        Book book = bookRepository.findByIdNotHidden(isbn)
                .orElseThrow(EntityNotFoundException::new);

        if (book.getBookStatus().equals(BookStatus.HIDDEN)) {
            throw new IllegalStateException("이미 hidden 처리된 도서입니다.");
        }

        book.setBookStatus(BookStatus.HIDDEN);
        bookRepository.save(book);

        // 로그 기록
        bookLogService.logHidden(book, employee);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // responseDto 변환 메서드
   private BookResponseDto toResponseDto(Book book) {
        return BookResponseDto.builder()
                .isbn(book.getBookIsbn())
                .bookTitle(book.getBookTitle())
                .categoryId(book.getCategoryId().getCategoryId())
                .categoryName(book.getCategoryId().getCategoryName())
                .authorName(book.getAuthorId().getAuthorName())
                .publisherName(book.getPublisherId().getPublisherName())
                .bookPrice(book.getBookPrice())
                .publishedDate(book.getPublishedDate())
                .coverUrl(book.getCoverImage() != null ? "/files/" + book.getCoverImage().getFileName() : null)
                .pageCount(book.getPageCount())
                .language(book.getLanguage())
                .description(book.getDescription())
                .bookStatus(book.getBookStatus().name())
                .policyId(
                        book.getDiscountPolicyId() != null
                                ? book.getDiscountPolicyId().getPolicyId()
                                : null
                )
                .build();
    }

    // 표지 저장
    private UploadFile saveAndRecordCoverImage(MultipartFile file, String isbn) throws IOException {
        String fileType = file.getContentType();
        if (fileType == null || !fileType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID() + "_" + originalName;

        Path saveDir = Paths.get(uploadDir);
        if (!Files.exists(saveDir)) Files.createDirectories(saveDir);

        Path filePath = saveDir.resolve(uuid);
        file.transferTo(filePath.toFile());

        UploadFile uploadFile = UploadFile.builder()
                .originalName(originalName)
                .fileName(uuid)
                .filePath(filePath.toString())
                .fileType(fileType)
                .fileSize(file.getSize())
                .build();
        uploadFileRepository.save(uploadFile);

        return uploadFile;
    }

}
