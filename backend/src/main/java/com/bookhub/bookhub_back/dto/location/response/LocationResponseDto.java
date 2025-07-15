package com.bookhub.bookhub_back.dto.location.response;

import com.bookhub.bookhub_back.common.enums.DisplayType;
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
    private String hall;
    private String section;
    private DisplayType displayType;
    private String note;
}
