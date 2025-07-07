package com.bookhub.bookhub_back.service;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.position.response.PositionResponseDto;

import java.util.List;

public interface PositionService {

    ResponseDto<List<PositionResponseDto>> getAllPosition();
}
