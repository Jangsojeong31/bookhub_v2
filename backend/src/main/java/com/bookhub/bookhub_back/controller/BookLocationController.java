package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.location.request.LocationCreateRequestDto;
import com.bookhub.bookhub_back.dto.location.request.LocationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.location.response.LocationCreateResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationDetailResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationUpdateResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.BookLocationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class BookLocationController {
    private final BookLocationService bookLocationService;

    private static final String BOOK_LOCATION_MANAGER = ApiMappingPattern.MANAGER + "/locations";
    private static final String BOOK_LOCATION_COMMON = ApiMappingPattern.COMMON + "/locations";

    // 진열위치 생성
    @PostMapping(BOOK_LOCATION_MANAGER)
    @Operation(summary = "진열위치 생성")
    public ResponseEntity<ResponseDto<LocationCreateResponseDto>> createLocation(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody LocationCreateRequestDto dto
    ){
        ResponseDto<LocationCreateResponseDto> response = bookLocationService.createLocation(userPrincipal, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 진열위치 수정
    @PutMapping(BOOK_LOCATION_MANAGER + "/{locationId}")
    @Operation(summary = "진열위치 수정")
    public ResponseEntity<ResponseDto<LocationUpdateResponseDto>> updateLocation(
            @PathVariable Long locationId,
            @Valid @RequestBody LocationUpdateRequestDto dto
    ){
        ResponseDto<LocationUpdateResponseDto> response = bookLocationService.updateLocation(locationId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점별 진열위치 전체조회
    @GetMapping(BOOK_LOCATION_COMMON)
    @Operation(summary = "진열위치 조회 (지점별)")
    public ResponseEntity<ResponseDto<List<LocationResponseDto>>> searchBranchBooksByTitle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false, defaultValue = "")  String keyword
    ){
        ResponseDto<List<LocationResponseDto>> response = bookLocationService.searchBranchBooksByTitle(userPrincipal, keyword);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점별 진열위치 단건조회
    @GetMapping(BOOK_LOCATION_COMMON + "/{locationId}")
    @Operation(summary = "진열위치 단건 조회")
    public ResponseEntity<ResponseDto<LocationDetailResponseDto>> getLocation(
            @PathVariable Long locationId
    ){
        ResponseDto<LocationDetailResponseDto> response = bookLocationService.getLocation(locationId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 진열위치 삭제
    @DeleteMapping(BOOK_LOCATION_MANAGER + "/{locationId}")
    @Operation(summary = "진열위치 삭제")
    public ResponseEntity<ResponseDto<Void>> deleteLocation(
            @PathVariable Long locationId
    ){
        ResponseDto<Void> response = bookLocationService.deleteLocation(locationId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
