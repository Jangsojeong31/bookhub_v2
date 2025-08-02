package com.bookhub.bookhub_back.controller;

import com.bookhub.bookhub_back.common.constants.ApiMappingPattern;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiMappingPattern.V1 + ApiMappingPattern.AUTH)
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        return "GitHub Action Test Success!";
    }
}
