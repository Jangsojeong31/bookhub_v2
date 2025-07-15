package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.location.request.LocationCreateRequestDto;
import com.bookhub.bookhub_back.dto.location.request.LocationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.location.response.LocationCreateResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationDetailResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationUpdateResponseDto;
import com.bookhub.bookhub_back.security.UserPrincipal;
import jakarta.validation.Valid;

import java.util.List;

public interface BookLocationService {

    ResponseDto<LocationCreateResponseDto> createLocation(UserPrincipal userPrincipal, @Valid LocationCreateRequestDto dto);

    ResponseDto<LocationUpdateResponseDto> updateLocation(Long locationId, @Valid LocationUpdateRequestDto dto);

    ResponseDto<List<LocationResponseDto>> searchBranchBooksByTitle(UserPrincipal userPrincipal, String keyword);

    ResponseDto<LocationDetailResponseDto> getLocation(Long locationId);

    ResponseDto<Void> deleteLocation(Long locationId);
}
