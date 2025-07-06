package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.publisher.request.PublisherRequestDto;
import com.bookhub.bookhub_back.dto.publisher.response.PublisherResponseDto;
import jakarta.validation.Valid;

public interface PublisherService {

    ResponseDto<PublisherResponseDto> createPublisher(@Valid PublisherRequestDto dto);

    ResponseDto<PublisherResponseDto> getPublishersByName(String publisherName);

    ResponseDto<PublisherResponseDto> updatePublisher(Long publisherId, @Valid PublisherRequestDto dto);

    ResponseDto<Void> deletePublisher(Long publisherId);
}
