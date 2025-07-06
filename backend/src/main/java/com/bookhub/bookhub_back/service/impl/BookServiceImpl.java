package com.bookhub.bookhub_back.service.impl;
import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.BookStatus;
import com.bookhub.bookhub_back.common.enums.FileTargetType;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.book.request.BookRequestDto;
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

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final EmployeeRepository employeeRepository;
    private final BookCategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final UploadFileRepository uploadFileRepository;

    @Value("${file.upload.path}")
    private String uploadDir;

    @Override
    public ResponseDto<BookResponseDto> createBook(BookRequestDto dto, String loginId, MultipartFile coverImageFile)
            throws IOException {
        Employee employee = employeeRepository.findByLoginId(loginId)
                .orElseThrow(() -> new EntityNotFoundException());

        BookCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException());

        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException());

        Publisher publisher = publisherRepository.findById(dto.getPublisherId())
                .orElseThrow(() -> new EntityNotFoundException());

            String coverUrl = coverImageFile != null && !coverImageFile.isEmpty()
                    ? saveAndRecordCoverImage(coverImageFile, dto.getIsbn())
                    : null;

        Book newBook = Book.builder()
                .bookIsbn(dto.getIsbn())
                .bookTitle(dto.getBookTitle())
                .categoryId(category)
                .authorId(author)
                .publisherId(publisher)
                .bookPrice(dto.getBookPrice())
                .publishedDate(dto.getPublishedDate())
                .coverUrl(coverUrl)
                .pageCount(dto.getPageCount())
                .language(dto.getLanguage())
                .description(dto.getDescription())
                .build();

        Book savedBook = bookRepository.save(newBook);

        BookResponseDto responseDto = toResponseDto(savedBook);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<BookResponseDto> getBookByIsbn(String isbn) {
        return null;
    }

    @Override
    public ResponseDto<List<BookResponseDto>> searchBooks(String keyword) {
        return null;
    }

    @Override
    public ResponseDto<BookResponseDto> updateBook(String isbn, BookRequestDto dto, String loginId, MultipartFile newCoverImageFile) {
        return null;
    }

    @Override
    public ResponseDto<Void> hideBook(String isbn, String loginId) {
        return null;
    }

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
                .policyId(book.getDiscountPolicyId().getPolicyId())
                .build();
    }

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
