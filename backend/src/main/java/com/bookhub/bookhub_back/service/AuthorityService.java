package com.bookhub.bookhub_back.service;


import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.authority.response.AuthorityResponseDto;

import java.util.List;

public interface AuthorityService {

    ResponseDto<List<AuthorityResponseDto>> getAllAuthorities();
}
