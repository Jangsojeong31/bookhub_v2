package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.common.util.DateUtils;

import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.entity.EmployeeChangeLog;
import com.bookhub.bookhub_back.entity.EmployeeExitLog;
import com.bookhub.bookhub_back.entity.EmployeeSignUpApproval;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

}
