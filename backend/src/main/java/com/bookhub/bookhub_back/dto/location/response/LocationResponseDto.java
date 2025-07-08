package com.bookhub.bookhub_back.dto.location.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationResponseDto {
    private Long locationId;
    private String bookTitle;
    private String floor;
}
