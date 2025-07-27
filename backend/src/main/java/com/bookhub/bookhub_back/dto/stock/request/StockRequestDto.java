package com.bookhub.bookhub_back.dto.stock.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StockRequestDto {
    @NotNull(message = "재고 이동 타입은 필수입니다.")
    private String type;
    @NotNull(message = "직원은 필수입니다.")
    private Long employeeId;
    @NotBlank(message = "isbn은 필수입니다.")
    private String bookIsbn;
    @NotNull(message = "지점은 필수입니다.")
    private Long branchId;
    @NotNull(message = "재고량은 필수입니다.")
    private Long amount;
    @NotBlank(message = "세부 설명은 필수입니다.")
    private String description;
}
