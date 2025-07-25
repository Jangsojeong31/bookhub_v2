package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.common.enums.ChangeType;
import com.bookhub.bookhub_back.common.enums.EmployeeStatus;
import com.bookhub.bookhub_back.common.enums.IsApproved;
import com.bookhub.bookhub_back.common.util.DateUtils;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeOrganizationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeSignUpApprovalRequestDto;
import com.bookhub.bookhub_back.dto.employee.request.EmployeeStatusUpdateRequestDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeListResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeResponseDto;
import com.bookhub.bookhub_back.dto.employee.response.EmployeeSignUpApprovalsResponseDto;
import com.bookhub.bookhub_back.entity.*;
import com.bookhub.bookhub_back.repository.*;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeSignUpApprovalRepository employeeSignUpApprovalRepository;
    private final BranchRepository branchRepository;
    private final PositionRepository positionRepository;
    private final AuthorityRepository authorityRepository;
    private final EmployeeChangeLogRepository employeeChangeLogRepository;
    private final EmployeeExitLogRepository employeeExitLogRepository;
    private final AlertRepository alertRepository;
    private final AlertService alertService;

    @Override
    public ResponseDto<List<EmployeeListResponseDto>> searchEmployee(
            String name, String branchName, String positionName, String authorityName, EmployeeStatus status
    ) {
        List<Employee> employees = employeeRepository.searchEmployees(name, branchName, positionName, authorityName, status);

        List<EmployeeListResponseDto> responseDtos = employees.stream()
                .filter(employee -> IsApproved.APPROVED == employee.getIsApproved())
                .map(employee -> EmployeeListResponseDto.builder()
                        .employeeId(employee.getEmployeeId())
                        .employeeNumber(employee.getEmployeeNumber())
                        .employeeName(employee.getName())
                        .branchName(employee.getBranchId().getBranchName())
                        .positionName(employee.getPositionId().getPositionName())
                        .authorityName(employee.getPositionId().getAuthority().getAuthorityName())
                        .status(employee.getStatus())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<List<EmployeeSignUpApprovalsResponseDto>> getPendingEmployee() {
        List<EmployeeSignUpApproval> employees = employeeSignUpApprovalRepository.findAllByIsApproved(IsApproved.PENDING);

        List<EmployeeSignUpApprovalsResponseDto> responseDtos = employees.stream()
                .map(employee -> EmployeeSignUpApprovalsResponseDto.builder()
                        .approvalId(employee.getApprovalId())
                        .employeeId(employee.getEmployeeId().getEmployeeId())
                        .employeeNumber(employee.getEmployeeId().getEmployeeNumber())
                        .employeeName(employee.getEmployeeId().getName())
                        .branchName(employee.getEmployeeId().getBranchId().getBranchName())
                        .email(employee.getEmployeeId().getEmail())
                        .phoneNumber(employee.getEmployeeId().getPhoneNumber())
                        .appliedAt(DateUtils.format(employee.getEmployeeId().getCreatedAt()))
                        .isApproved(employee.getIsApproved())
                        .build())
                .collect(Collectors.toList());


        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<EmployeeResponseDto> getEmployeeById(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);

        EmployeeResponseDto responseDto = EmployeeResponseDto.builder()
                .employeeId(employee.getEmployeeId())
                .employeeNumber(employee.getEmployeeNumber())
                .employeeName(employee.getName())
                .branchId(employee.getBranchId().getBranchId())
                .branchName(employee.getBranchId().getBranchName())
                .positionId(employee.getPositionId().getPositionId())
                .positionName(employee.getPositionId().getPositionName())
                .authorityId(employee.getPositionId().getAuthority().getAuthorityId())
                .authorityName(employee.getPositionId().getAuthority().getAuthorityName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .birthDate(employee.getBirthDate())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, responseDto);
    }

    @Override
    @Transactional
    public ResponseDto<EmployeeSignUpApprovalsResponseDto> updateApproval(Long employeeId, EmployeeSignUpApprovalRequestDto dto, UserPrincipal userPrincipal) {
        EmployeeListResponseDto responseDto = null;

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);

        EmployeeSignUpApproval employeeSignUpApproval
                = employeeSignUpApprovalRepository.findAllByEmployeeId_EmployeeIdAndIsApproved(employee.getEmployeeId(), IsApproved.PENDING)
                .orElseThrow(EntityNotFoundException::new);

        Employee authorizerEmployee = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        String deniedReason = dto.getDeniedReason();

        if (dto.getIsApproved().equals(IsApproved.APPROVED)) {
            if(deniedReason != null && !deniedReason.isBlank()) {
                throw new IllegalArgumentException("거절 사유는 승인 거절 시 입력할 수 있습니다.");
            }
        } else if (dto.getIsApproved().equals(IsApproved.DENIED)) {
            if(deniedReason == null || deniedReason.isBlank()) {
                throw new IllegalArgumentException("승인 거절 사유를 입력해주세요.");
            }
                employeeSignUpApproval.setDeniedReason(deniedReason);
        } else {
            throw new IllegalStateException("잘못된 승인 상태입니다.");
        }

        employee.setIsApproved(dto.getIsApproved());
        employeeSignUpApproval.setAuthorizerId(authorizerEmployee);
        employeeSignUpApproval.setIsApproved(dto.getIsApproved());


        employeeRepository.save(employee);
        employeeSignUpApprovalRepository.save(employeeSignUpApproval);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }

    @Override
    @Transactional
    public ResponseDto<Void> updateOrganization(Long employeeId, EmployeeOrganizationUpdateRequestDto dto, UserPrincipal userPrincipal) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);

        Employee authorizer = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        Long preBranchId = employee.getBranchId().getBranchId();
        Long prePositionId = employee.getPositionId().getPositionId();
        Long preAuthorityId = employee.getPositionId().getAuthority().getAuthorityId();

        // 지점 변경 시
        if (!dto.getBranchId().equals(preBranchId)) {
            employee.setBranchId(branchRepository.findById(dto.getBranchId())
                    .orElseThrow(EntityNotFoundException::new));

            // 직원 정보 변경 로그 생성
            EmployeeChangeLog employeeChangeLog = EmployeeChangeLog.builder()
                    .employeeId(employee)
                    .authorizerId(authorizer)
                    .changeType(ChangeType.BRANCH_CHANGE)
                    .previousBranchId(branchRepository.findById(preBranchId)
                            .orElseThrow(IllegalArgumentException::new))
                    .build();

            employeeChangeLogRepository.save(employeeChangeLog);

            // 알림 생성
            alertService.createAlert(AlertCreateRequestDto.builder()
                    .employeeId(employee.getEmployeeId())
                    .alertType(String.valueOf(AlertType.CHANGE_BRANCH_SUCCESS))
                    .alertTargetTable("EMPLOYEES")
                    .targetPk(employee.getEmployeeId())
                    .message("지점이 [" + employee.getBranchId().getBranchName() + "]로 변경되었습니다.")
                    .build()
            );
        }

        // 직급 변경 시
        if (!dto.getPositionId().equals(prePositionId)) {
            employee.setPositionId(positionRepository.findById(dto.getPositionId())
                    .orElseThrow(EntityNotFoundException::new));

            // 직원 정보 변경 로그 생성
            EmployeeChangeLog employeeChangeLog = EmployeeChangeLog.builder()
                    .employeeId(employee)
                    .authorizerId(authorizer)
                    .changeType(ChangeType.POSITION_CHANGE)
                    .previousPositionId(positionRepository.findById(prePositionId)
                            .orElseThrow(IllegalArgumentException::new))
                    .build();

            employeeChangeLogRepository.save(employeeChangeLog);

            // 알림 생성
            alertService.createAlert(AlertCreateRequestDto.builder()
                    .employeeId(employee.getEmployeeId())
                    .alertType(String.valueOf(AlertType.CHANGE_POSITION_SUCCESS))
                    .alertTargetTable("EMPLOYEES")
                    .targetPk(employee.getEmployeeId())
                    .message("직급이 [" + employee.getPositionId().getPositionName() + "]로 변경되었습니다.")
                    .build()
            );
        }

//        // 권한 변경 시
//        if (!dto.getAuthorityId().equals(preAuthorityId)) {
//            employee.setAuthorityId(authorityRepository.findById(dto.getAuthorityId())
//                    .orElseThrow(EntityNotFoundException::new));
//
//            // 직원 정보 변경 로그 생성
//            EmployeeChangeLog employeeChangeLog = EmployeeChangeLog.builder()
//                    .employeeId(employee)
//                    .authorizerId(authorizer)
//                    .changeType(ChangeType.AUTHORITY_CHANGE)
//                    .previousAuthorityId(authorityRepository.findById(preAuthorityId)
//                            .orElseThrow(IllegalArgumentException::new))
//                    .build();
//
//            employeeChangeLogRepository.save(employeeChangeLog);
//
//            // 알림 생성
//            alertService.createAlert(AlertCreateRequestDto.builder()
//                    .employeeId(employee.getEmployeeId())
//                    .alertType(String.valueOf(AlertType.CHANGE_PERMISSION_SUCCESS))
//                    .alertTargetTable("EMPLOYEES")
//                    .targetPk(employee.getEmployeeId())
//                    .message("권한이 [" + employee.getAuthorityId().getAuthorityName() + "]로 변경되었습니다.")
//                    .build()
//            );
//        }

        employeeRepository.save(employee);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }

    // 퇴사 처리
    @Override
    @Transactional
    public ResponseDto<Void> updateStatus(Long employeeId, EmployeeStatusUpdateRequestDto dto, UserPrincipal userPrincipal) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(EntityNotFoundException::new);

        Employee authorizer = employeeRepository.findByLoginId(userPrincipal.getLoginId())
                .orElseThrow(EntityNotFoundException::new);

        EmployeeStatus status = employee.getStatus();

        if (status == EmployeeStatus.EXITED) {
            throw new IllegalStateException("이미 퇴사 처리되었습니다.");
        }

        if (dto.getStatus() == EmployeeStatus.EXITED) {
            employee.setStatus(dto.getStatus());
            employee.setPositionId(positionRepository.findByAuthority_AuthorityName("NONE"));
        } else {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }

        employeeRepository.save(employee);

        EmployeeExitLog employeeExitLog = EmployeeExitLog.builder()
                .employeeId(employee)
                .appliedAt(employee.getCreatedAt())
                .authorizerId(authorizer)
                .exitReason(dto.getExitReason())
                .build();
        employeeExitLogRepository.save(employeeExitLog);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS);
    }
}
