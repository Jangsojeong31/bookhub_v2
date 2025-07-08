package com.bookhub.bookhub_back.dto.location.request;

import com.bookhub.bookhub_back.common.enums.DisplayType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationCreateRequestDto {
    @NotBlank(message = "ISBN은 필수입니다.")
    private String bookIsbn;
    @NotBlank(message = "진열 층은 필수입니다.")
    private String floor;
    private String hall;
    private String section;
    private DisplayType displayType;
    private String note;
}
