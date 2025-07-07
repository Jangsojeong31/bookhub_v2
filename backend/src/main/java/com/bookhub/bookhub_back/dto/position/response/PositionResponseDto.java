package com.bookhub.bookhub_back.dto.position.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PositionResponseDto {
    private Long positionId;
    private String positionName;
}
