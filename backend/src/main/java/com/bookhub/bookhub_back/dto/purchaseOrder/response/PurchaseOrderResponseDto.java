package com.bookhub.bookhub_back.dto.purchaseOrder.response;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderResponseDto {
    private Long purchaseOrderId;
    private String branchName;
    private String branchLocation;
    private String employeeName;
    private String isbn;
    private String bookTitle;
    private Long bookPrice;
    private int purchaseOrderAmount;
    private Long purchaseOrderPrice;
    private PurchaseOrderStatus purchaseOrderStatus;
    private String purchaseOrderAt;
}
