package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.position.response.PositionResponseDto;
import com.bookhub.bookhub_back.entity.Position;
import com.bookhub.bookhub_back.repository.PositionRepository;
import com.bookhub.bookhub_back.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;

    @Override
    public ResponseDto<List<PositionResponseDto>> getAllPosition() {

        List<Position> positions = positionRepository.findAll();

        List<PositionResponseDto> responseDtos = positions.stream()
                .map(position -> PositionResponseDto.builder()
                        .positionId(position.getPositionId())
                        .positionName(position.getPositionName())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }
}
