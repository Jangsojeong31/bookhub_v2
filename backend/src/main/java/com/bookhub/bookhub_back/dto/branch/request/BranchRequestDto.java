package com.bookhub.bookhub_back.dto.branch.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class BranchRequestDto {
    @NotBlank(message = "지점명은 필수값입니다.")
    private String branchName;

    @NotBlank(message = "지점 주소는 필수값입니다.")
    private String branchLocation;
}
