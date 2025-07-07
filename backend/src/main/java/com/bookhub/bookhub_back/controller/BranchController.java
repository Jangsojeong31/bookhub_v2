package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.branch.request.BranchRequestDto;
import com.bookhub.bookhub_back.dto.branch.response.BranchResponseDto;
import com.bookhub.bookhub_back.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;

    // 지점 등록
    @PostMapping(ApiMappingPattern.ADMIN + "/branches")
    public ResponseEntity<ResponseDto<BranchResponseDto>> createBranch(
            @Valid @RequestBody BranchRequestDto dto
    ) {
        ResponseDto<BranchResponseDto> response = branchService.createBranch(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 주소로 지점 조회
    @GetMapping(ApiMappingPattern.AUTH + "/branches")
    public ResponseEntity<ResponseDto<List<BranchResponseDto>>> getBranchesByLocation(
            @RequestParam(required = false) String branchLocation
    ) {
        ResponseDto<List<BranchResponseDto>> response = branchService.getBranchesByLocation(branchLocation);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 단건 조회
    @GetMapping(ApiMappingPattern.ADMIN + "/branches/{branchId}")
    public ResponseEntity<ResponseDto<BranchResponseDto>> getBranchById(
            @PathVariable Long branchId
    ) {
        ResponseDto<BranchResponseDto> response = branchService.getBranchById(branchId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점 수정
    @PutMapping(ApiMappingPattern.ADMIN + "/branches/{branchId}")
    public ResponseEntity<ResponseDto<BranchResponseDto>> updateBranch(
            @PathVariable Long branchId,
            @Valid @RequestBody BranchRequestDto dto
    ) {
        ResponseDto<BranchResponseDto> response = branchService.updateBranch(branchId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
