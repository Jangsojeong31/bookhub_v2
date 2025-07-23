package com.bookhub.bookhub_back.dto.purchaseOrder.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PurchaseOrderRequestDto {
    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;
    @NotNull(message = "발주량은 필수입니다.")
    @Min(value = 1, message = "발주량은 1 이상이어야 합니다.")
    private int purchaseOrderAmount;
}
