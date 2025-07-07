package com.bookhub.bookhub_back.dto.authority.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorityResponseDto {
    private Long authorityId;
    private String authorityName;
}
