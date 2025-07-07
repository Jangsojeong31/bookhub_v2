package com.bookhub.bookhub_back.dto.publisher.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublisherResponseDto {
    private Long publisherId;
    private String publisherName;
}
