package com.bookhub.bookhub_back.service.impl;
import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.BookStatus;
import com.bookhub.bookhub_back.common.enums.FileTargetType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookCreateRequestDto;
import com.bookhub.bookhub_back.dto.book.request.BookUpdateRequestDto;
import com.bookhub.bookhub_back.dto.book.response.BookResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.provider.JwtProvider;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.service.BookLogService;
import com.bookhub.bookhub_back.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
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
    public ResponseDto<BookResponseDto> createBook(BookCreateRequestDto dto, String loginId, MultipartFile coverImageFile) throws IOException {
        Employee employee = employeeRepository.findByLoginId(loginId)
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

        String coverUrl = coverImageFile != null && !coverImageFile.isEmpty()
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
                .coverUrl(coverUrl)
                .pageCount(dto.getPageCount())
                .language(dto.getLanguage())
                .description(dto.getDescription())
                .bookStatus(BookStatus.ACTIVE)
                .discountPolicyId(policy)
                .build();

        Book savedBook = bookRepository.save(book);

        BookResponseDto responseDto = toResponseDto(savedBook);

//        bookLogService.logCreate(savedBook, employee);

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
    public ResponseDto<BookResponseDto> updateBook(String isbn, BookUpdateRequestDto dto, String loginId, MultipartFile coverImageFile) throws IOException {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        Book book = bookRepository.findById(dto.getIsbn())
                .orElseThrow(EntityNotFoundException::new);

        BookCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);

        DiscountPolicy policy = dto.getPolicyId() != null
                ? discountPolicyRepository.findById(dto.getPolicyId())
                .orElseThrow(EntityNotFoundException::new)
                : null;

        String coverUrl = coverImageFile != null && !coverImageFile.isEmpty()
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
        book.setCoverUrl(coverUrl);

        Book updatedBook = bookRepository.save(book);

        BookResponseDto responseDto = toResponseDto(updatedBook);

        // 로그 기록
//        if (!oldPrice.equals(dto.getBookPrice())) {
//            bookLogService.logPriceChange(updatedBook, oldPrice, employee);
//        }
//
//        DiscountPolicy newPolicy = updatedBook.getDiscountPolicyId();
//        Integer newRate = newPolicy != null ? newPolicy.getDiscountPercent() : null;
//        if ((oldRate != null && !oldRate.equals(newRate)) || (oldRate == null && newRate != null)) {
//            bookLogService.logDiscountChange(updatedBook, oldRate != null ? oldRate : 0, newPolicy, employee);
//        }
//
//        if (!oldStatus.equals(dto.getBookStatus())) {
//            bookLogService.logStatusChange(updatedBook, employee);
//        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 도서 삭제(hidden 처리)
    @Override
    @Transactional
    public ResponseDto<Void> hideBook(String isbn, String loginId) {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(EntityNotFoundException::new);

        Book book = bookRepository.findById(isbn)
                .orElseThrow(EntityNotFoundException::new);

        book.setBookStatus(BookStatus.HIDDEN);
        bookRepository.save(book);

//        bookLogService.logHidden(book, employee);

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
                .coverUrl(book.getCoverUrl())
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
    private String saveAndRecordCoverImage(MultipartFile file, String isbn) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String originalName = file.getOriginalFilename();
        String uuid = UUID.randomUUID() + "_" + originalName;
        String filePath = uploadDir + "/" + uuid;
        String fileType = file.getContentType();
        long fileSize = file.getSize();

        file.transferTo(new File(filePath));

        UploadFile uploadFile = UploadFile.builder()
                .originalName(originalName)
                .fileName(uuid)
                .filePath(filePath)
                .fileType(fileType)
                .fileSize(fileSize)
                .targetId(isbn)
                .targetType(FileTargetType.BOOK)
                .build();
        uploadFileRepository.save(uploadFile);

        return "/files/" + uuid;
    }
}
