package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.branch.request.BranchRequestDto;
import com.bookhub.bookhub_back.dto.branch.response.BranchResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface BranchService {

    ResponseDto<BranchResponseDto> createBranch(@Valid BranchRequestDto dto);

    ResponseDto<List<BranchResponseDto>> getBranchesByLocation(String branchLocation);

    ResponseDto<BranchResponseDto> getBranchById(Long branchId);

    ResponseDto<BranchResponseDto> updateBranch(Long branchId, @Valid BranchRequestDto dto);
}
