package com.bookhub.bookhub_back.dto.purchaseOrder.request;

import com.bookhub.bookhub_back.common.enums.PurchaseOrderStatus;
import lombok.Getter;

@Getter
public class PurchaseOrderApproveRequestDto {
    PurchaseOrderStatus status;
}
