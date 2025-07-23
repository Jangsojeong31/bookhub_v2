package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.branch.request.BranchRequestDto;
import com.bookhub.bookhub_back.dto.branch.response.BranchResponseDto;
import com.bookhub.bookhub_back.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
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

    private static final String BRANCH_ADMIN = ApiMappingPattern.ADMIN + "/branches";
    private static final String BRANCH_AUTH = ApiMappingPattern.AUTH + "/branches";

    // 지점 등록
    @PostMapping(BRANCH_ADMIN)
    @Operation(summary = "지점 생성")
    public ResponseEntity<ResponseDto<BranchResponseDto>> createBranch(
            @Valid @RequestBody BranchRequestDto dto
    ) {
        ResponseDto<BranchResponseDto> response = branchService.createBranch(dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 주소로 지점 조회
    @GetMapping(BRANCH_AUTH)
    @Operation(summary = "지점 조회 (주소로)")
    public ResponseEntity<ResponseDto<List<BranchResponseDto>>> getBranchesByLocation(
            @RequestParam(required = false) String branchLocation
    ) {
        ResponseDto<List<BranchResponseDto>> response = branchService.getBranchesByLocation(branchLocation);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 단건 조회
    @GetMapping(BRANCH_ADMIN + "/{branchId}")
    public ResponseEntity<ResponseDto<BranchResponseDto>> getBranchById(
            @PathVariable Long branchId
    ) {
        ResponseDto<BranchResponseDto> response = branchService.getBranchById(branchId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점 수정
    @PutMapping(BRANCH_ADMIN + "/{branchId}")
    @Operation(summary = "지점 수정")
    public ResponseEntity<ResponseDto<BranchResponseDto>> updateBranch(
            @PathVariable Long branchId,
            @Valid @RequestBody BranchRequestDto dto
    ) {
        ResponseDto<BranchResponseDto> response = branchService.updateBranch(branchId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
