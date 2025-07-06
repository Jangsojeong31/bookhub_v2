package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import com.bookhub.bookhub_back.entity.Publisher;
import com.bookhub.bookhub_back.exception.DuplicateResourceException;
import com.bookhub.bookhub_back.repository.PublisherRepository;
import com.bookhub.bookhub_back.service.PublisherService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    // 출판사 등록
    @Override
    public ResponseDto<PublisherResponseDto> createPublisher(PublisherRequestDto dto) {

        // 출판사 이름 중복 확인
        if(publisherRepository.existsByPublisherName(dto.getPublisherName())) {
            throw new DuplicateResourceException("이미 등록된 출판사 이름입니다.");
        }

        Publisher newPublisher = Publisher.builder()
                .publisherName(dto.getPublisherName())
                .build();

        Publisher savedPublisher = publisherRepository.save(newPublisher);

        PublisherResponseDto responseDto = PublisherResponseDto.builder()
                .publisherId(savedPublisher.getPublisherId())
                .publisherName(savedPublisher.getPublisherName())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 출판사 전체 조회
    @Override
    public ResponseDto<List<PublisherResponseDto>> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();

        List<PublisherResponseDto> responseDtos = publishers.stream()
                .map(publisher -> PublisherResponseDto.builder()
                        .publisherId(publisher.getPublisherId())
                        .publisherName(publisher.getPublisherName())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 출판사 이름으로 조회
    @Override
    public ResponseDto<List<PublisherResponseDto>> getPublishersByName(String publisherName) {

        if (publisherName == null && publisherName.isBlank()) {
            return ResponseDto.fail(ResponseCode.FAIL, "검색하실 출판사 이름을 입력해주세요.");
        }

        List<Publisher> publishers = publisherRepository.findAllByPublisherNameContaining(publisherName);

        List<PublisherResponseDto> responseDtos = publishers.stream()
                .map(publisher -> PublisherResponseDto.builder()
                        .publisherId(publisher.getPublisherId())
                        .publisherName(publisher.getPublisherName())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    // 출판사 수정
    @Override
    public ResponseDto<PublisherResponseDto> updatePublisher(Long publisherId, PublisherRequestDto dto) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new EntityNotFoundException());

        // 출판사 이름 중복 확인
        if(!publisher.getPublisherName().equals(dto.getPublisherName()) &&
                publisherRepository.existsByPublisherName(dto.getPublisherName())) {
            return ResponseDto.fail(ResponseCode.DUPLICATED_RESOURCE, "이미 등록된 출판사 이름입니다.");
        }

        publisher.setPublisherName(dto.getPublisherName());

        Publisher updatedPublisher = publisherRepository.save(publisher);

        PublisherResponseDto responseDto = PublisherResponseDto.builder()
                .publisherId(updatedPublisher.getPublisherId())
                .publisherName(updatedPublisher.getPublisherName())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // 출판사 삭제
    @Override
    public ResponseDto<Void> deletePublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(() -> new EntityNotFoundException());

        publisherRepository.delete(publisher);
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }
}
