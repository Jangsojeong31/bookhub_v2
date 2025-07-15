package com.bookhub.bookhub_back.dto.location.request;

import com.bookhub.bookhub_back.common.enums.DisplayType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocationCreateRequestDto {
    @NotBlank(message = "ISBN은 필수입니다.")
    private String bookIsbn;
    @NotBlank(message = "진열 층은 필수입니다.")
    private String floor;
    @NotBlank(message = "진열 홀은 필수입니다.")
    private String hall;
    @NotBlank(message = "진열 섹션은 필수입니다.")
    private String section;
    @NotNull(message = "진열 형태은 필수입니다.")
    private DisplayType displayType;
    private String note;
}
