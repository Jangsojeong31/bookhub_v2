package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import com.bookhub.bookhub_back.entity.Author;
import com.bookhub.bookhub_back.repository.AuthorRepository;
import com.bookhub.bookhub_back.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Override
    public ResponseDto<AuthorResponseDto> createAuthor(AuthorRequestDto dto) {
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
        List<Author> authors = authorRepository.findAllByAuthorNameContaining(authorName);

        List<AuthorResponseDto> responseDtos = authors.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<AuthorResponseDto> updateAuthor(Long authorId, AuthorRequestDto dto) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException());

        author.setAuthorName(dto.getAuthorName());
        author.setAuthorEmail(dto.getAuthorEmail());

        Author updatedAuthor = authorRepository.save(author);

        AuthorResponseDto responseDto = toResponseDto(updatedAuthor);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<Void> deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException());

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
