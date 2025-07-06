package com.bookhub.bookhub_back.dto.publisher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PublisherResponseDto {
    private Long publisherId;
    private String publisherName;
}
