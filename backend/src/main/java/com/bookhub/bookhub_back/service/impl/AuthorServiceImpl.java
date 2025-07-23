package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;

import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import com.bookhub.bookhub_back.entity.Author;
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.exception.InvalidSearchConditionException;
import com.bookhub.bookhub_back.exception.ReferencedEntityException;
import com.bookhub.bookhub_back.repository.AuthorRepository;
import com.bookhub.bookhub_back.repository.BookRepository;
import com.bookhub.bookhub_back.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Override
    public ResponseDto<AuthorResponseDto> createAuthor(AuthorRequestDto dto) {
        if (authorRepository.existsByAuthorEmail(dto.getAuthorEmail())) {
            throw new DuplicateEntityException("이미 존재하는 작가 이메일입니다.");
        }

        Author newAuthor = Author.builder()
                .authorName(dto.getAuthorName())
                .authorEmail(dto.getAuthorEmail())
                .build();

        Author savedAuthor = authorRepository.save(newAuthor);

        AuthorResponseDto responseDto = toResponseDto(savedAuthor);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<AuthorResponseDto>> getAuthorsByName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            throw new InvalidSearchConditionException(ResponseMessageKorean.INVALID_SEARCH_CONDITION);
        }

        List<Author> authors = authorRepository.findAllByAuthorNameContaining(authorName);

        List<AuthorResponseDto> responseDtos = authors.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    @Transactional
    public ResponseDto<AuthorResponseDto> updateAuthor(Long authorId, AuthorRequestDto dto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(EntityNotFoundException::new);

        boolean isEmailUsedByAnother = authorRepository.existsByAuthorEmailAndAuthorIdNot(dto.getAuthorEmail(), authorId);
        if (isEmailUsedByAnother) {
            throw new DuplicateEntityException("이미 존재하는 작가 이메일입니다.");
        }

        author.setAuthorName(dto.getAuthorName());
        author.setAuthorEmail(dto.getAuthorEmail());

        Author updatedAuthor = authorRepository.save(author);

        AuthorResponseDto responseDto = toResponseDto(updatedAuthor);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<Void> deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(EntityNotFoundException::new);

        if (bookRepository.existsByAuthorId_AuthorId(author.getAuthorId())) {
            throw new ReferencedEntityException();
        }

        authorRepository.delete(author);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // responseDto 변환 메서드
    private AuthorResponseDto toResponseDto(Author author) {
        return AuthorResponseDto.builder()
                .authorId(author.getAuthorId())
                .authorName(author.getAuthorName())
                .authorEmail(author.getAuthorEmail())
                .build();
    }
}
