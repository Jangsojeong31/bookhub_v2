package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.location.request.LocationCreateRequestDto;
import com.bookhub.bookhub_back.dto.location.request.LocationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.location.response.LocationCreateResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationDetailResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationUpdateResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface BookLocationService {

    ResponseDto<LocationCreateResponseDto> createLocation(Long branchId, @Valid LocationCreateRequestDto dto);

    ResponseDto<LocationUpdateResponseDto> updateLocation(Long branchId, Long locationId, @Valid LocationUpdateRequestDto dto);

    ResponseDto<List<LocationResponseDto>> searchBranchBooksByTitle(Long branchId, String keyword);

    ResponseDto<LocationDetailResponseDto> getLocation(Long branchId, Long locationId);

    ResponseDto<Void> deleteLocation(Long branchId, Long locationId);
}
