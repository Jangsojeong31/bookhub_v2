package com.bookhub.bookhub_back.dto.location.response;

import com.bookhub.bookhub_back.common.enums.DisplayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LocationCreateResponseDto {
    private String bookTitle;
    private String floor;
    private String hall;
    private String section;
    private DisplayType type;
}
