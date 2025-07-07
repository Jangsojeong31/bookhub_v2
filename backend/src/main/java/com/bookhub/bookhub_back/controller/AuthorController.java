package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import com.bookhub.bookhub_back.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.ADMIN + "/authors")
@RequiredArgsConstructor
public class AuthorController {
    private final AuthorService authorService;
}
