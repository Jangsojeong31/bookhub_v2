package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.author.request.AuthorRequestDto;
import com.bookhub.bookhub_back.dto.author.response.AuthorResponseDto;
import com.bookhub.bookhub_back.entity.Author;
import com.bookhub.bookhub_back.exception.DuplicateResourceException;
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

    // 작가 등록
    @Override
    public ResponseDto<AuthorResponseDto> createAuthor(AuthorRequestDto dto) {

        // 이메일 중복 확인
        if(authorRepository.existsByAuthorEmail(dto.getAuthorEmail())){
            throw new DuplicateResourceException("이미 등록된 이메일입니다.");
        }

        Author newAuthor = Author.builder()
                .authorName(dto.getAuthorName())
                .authorEmail(dto.getAuthorEmail())
                .build();

        Author savedAuthor = authorRepository.save(newAuthor);

        AuthorResponseDto responseDto = AuthorResponseDto.builder()
                .authorId(savedAuthor.getAuthorId())
                .authorName(savedAuthor.getAuthorName())
                .authorEmail(savedAuthor.getAuthorEmail())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 작가 이름으로 조회
    @Override
    public ResponseDto<List<AuthorResponseDto>> getAuthorsByName(String authorName) {

        if (authorName == null && authorName.isBlank()) {
            return ResponseDto.fail(ResponseCode.FAIL, "검색하실 저자 이름을 입력해주세요.");
        }

        List<Author> authors = authorRepository.findAllByAuthorNameContaining(authorName);

        List<AuthorResponseDto> responseDtos = authors.stream()
                .map(author -> AuthorResponseDto.builder()
                        .authorId(author.getAuthorId())
                        .authorName((author.getAuthorName()))
                        .authorEmail(author.getAuthorEmail())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 작가 수정
    @Override
    public ResponseDto<AuthorResponseDto> updateAuthor(Long authorId, AuthorRequestDto dto) {

        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException());

        // 이메일 중복 확인
        if(!author.getAuthorEmail().equals(dto.getAuthorEmail()) &&
                authorRepository.existsByAuthorEmail(dto.getAuthorEmail())) {
            throw new DuplicateResourceException("이미 등록된 이메일입니다.");
        }

        author.setAuthorName(dto.getAuthorName());
        author.setAuthorEmail(dto.getAuthorEmail());

        Author updatedAuthor = authorRepository.save(author);

        AuthorResponseDto responseDto = AuthorResponseDto.builder()
                .authorId(updatedAuthor.getAuthorId())
                .authorName(updatedAuthor.getAuthorName())
                .authorEmail(updatedAuthor.getAuthorEmail())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<Void> deleteAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException());

        authorRepository.delete(author);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
}
