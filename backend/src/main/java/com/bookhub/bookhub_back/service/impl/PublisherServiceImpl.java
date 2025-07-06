package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import com.bookhub.bookhub_back.repository.PublisherRepository;
import com.bookhub.bookhub_back.service.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;

    @Override
    public ResponseDto<PublisherResponseDto> createPublisher(PublisherRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<PublisherResponseDto> getPublishersByName(String publisherName) {
        return null;
    }

    @Override
    public ResponseDto<PublisherResponseDto> updatePublisher(Long publisherId, PublisherRequestDto dto) {
        return null;
    }

    @Override
    public ResponseDto<Void> deletePublisher(Long publisherId) {
        return null;
    }
}
