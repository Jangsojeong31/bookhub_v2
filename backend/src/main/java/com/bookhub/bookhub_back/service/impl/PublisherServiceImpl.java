package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import com.bookhub.bookhub_back.entity.Publisher;
import com.bookhub.bookhub_back.exception.BusinessException;
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.exception.ReferencedEntityException;
import com.bookhub.bookhub_back.repository.BookRepository;
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
    private final BookRepository bookRepository;

    @Override
    public ResponseDto<PublisherResponseDto> createPublisher(PublisherRequestDto dto) {
        if (publisherRepository.existsByPublisherName(dto.getPublisherName())) {
            throw new DuplicateEntityException("이미 존재하는 출판사입니다.");
        }

        Publisher newPublisher = Publisher.builder()
                .publisherName(dto.getPublisherName())
                .build();

        Publisher savedPublisher = publisherRepository.save(newPublisher);

        PublisherResponseDto responseDto = toResponseDto(savedPublisher);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<PublisherResponseDto>> getPublishers(String publisherName) {

        List<Publisher> publishers = publisherName == null || publisherName.isBlank()
                ? publisherRepository.findAll()
                : publisherRepository.findByPublisherNameContaining(publisherName);

        List<PublisherResponseDto> responseDtos = publishers.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<PublisherResponseDto> updatePublisher(Long publisherId, PublisherRequestDto dto) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(EntityNotFoundException::new);

        boolean isPublisherNameExists = publisherRepository.existsByPublisherNameAndPublisherIdNot(dto.getPublisherName(), publisherId);
        if (isPublisherNameExists) {
            throw new DuplicateEntityException("이미 존재하는 출판사입니다.");
        }

        publisher.setPublisherName(dto.getPublisherName());

        Publisher updatedPublisher = publisherRepository.save(publisher);

        PublisherResponseDto responseDto = toResponseDto(updatedPublisher);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<Void> deletePublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId)
                .orElseThrow(EntityNotFoundException::new);

        if (bookRepository.existsByPublisherId_PublisherId(publisher.getPublisherId())) {
            throw new ReferencedEntityException();
        }

        publisherRepository.delete(publisher);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // responseDto 변환 메서드
    private PublisherResponseDto toResponseDto(Publisher publisher) {
        return PublisherResponseDto.builder()
                .publisherId(publisher.getPublisherId())
                .publisherName(publisher.getPublisherName())
                .build();
    }
}
