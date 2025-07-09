package com.bookhub.bookhub_back.dto.reception.request;

import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.PurchaseOrderApproval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class ReceptionCreateRequestDto {
    private Long purchaseOrderApprovalId;
    private Branch receivingBranch;
}
