package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.location.request.LocationCreateRequestDto;
import com.bookhub.bookhub_back.dto.location.request.LocationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.location.response.LocationCreateResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationDetailResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationUpdateResponseDto;
import com.bookhub.bookhub_back.service.BookLocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.V1)
@RequiredArgsConstructor
public class BookLocationController {
    private final BookLocationService bookLocationService;

    private final String BOOK_LOCATION_MANAGER = ApiMappingPattern.MANAGER + "/locations";
    private final String BOOK_LOCATION_COMMON = ApiMappingPattern.COMMON + "/locations";

    // 진열위치 생성
    @PostMapping(BOOK_LOCATION_MANAGER)
    public ResponseEntity<ResponseDto<LocationCreateResponseDto>> createLocation(
            @RequestParam Long branchId,
            @Valid @RequestBody LocationCreateRequestDto dto
    ){
        ResponseDto<LocationCreateResponseDto> response = bookLocationService.createLocation(branchId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.CREATED, response);
    }

    // 진열위치 수정
    @PutMapping(BOOK_LOCATION_MANAGER + "/{locationId}")
    public ResponseEntity<ResponseDto<LocationUpdateResponseDto>> updateLocation(
            @RequestParam Long branchId,
            @PathVariable Long locationId,
            @Valid @RequestBody LocationUpdateRequestDto dto
    ){
        ResponseDto<LocationUpdateResponseDto> response = bookLocationService.updateLocation(branchId,locationId, dto);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점별 진열위치 전체조회
    @GetMapping(BOOK_LOCATION_COMMON)
    public ResponseEntity<ResponseDto<List<LocationResponseDto>>> searchBranchBooksByTitle(
            @RequestParam Long branchId,
            @RequestParam(required = false, defaultValue = "")  String keyword
    ){
        ResponseDto<List<LocationResponseDto>> response = bookLocationService.searchBranchBooksByTitle(branchId, keyword);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 지점별 진열위치 단건조회
    @GetMapping(BOOK_LOCATION_COMMON + "/{locationId}")
    public ResponseEntity<ResponseDto<LocationDetailResponseDto>> getLocation(
            @RequestParam Long branchId,
            @PathVariable Long locationId
    ){
        ResponseDto<LocationDetailResponseDto> response = bookLocationService.getLocation(branchId,locationId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

    // 진열위치 삭제
    @DeleteMapping(BOOK_LOCATION_MANAGER + "/{locationId}")
    public ResponseEntity<ResponseDto<Void>> deleteLocation(
            @RequestParam Long branchId,
            @PathVariable Long locationId
    ){
        ResponseDto<Void> response = bookLocationService.deleteLocation(branchId, locationId);
        return ResponseDto.toResponseEntity(HttpStatus.OK, response);
    }

}
