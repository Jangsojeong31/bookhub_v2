package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.ExitReason;

import com.bookhub.bookhub_back.entity.EmployeeExitLog;
import com.bookhub.bookhub_back.repository.EmployeeExitLogRepository;
import com.bookhub.bookhub_back.service.EmployeeExitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeExitLogServiceImpl implements EmployeeExitLogService {

}
