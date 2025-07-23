package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.enums.AlertType;
import com.bookhub.bookhub_back.common.enums.PolicyType;

import com.bookhub.bookhub_back.dto.PageResponseDto;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.alert.request.AlertCreateRequestDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyCreateRequestDto;
import com.bookhub.bookhub_back.dto.policy.request.DiscountPolicyUpdateRequestDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyDetailResponseDto;
import com.bookhub.bookhub_back.dto.policy.response.DiscountPolicyListResponseDto;
import com.bookhub.bookhub_back.entity.DiscountPolicy;
import com.bookhub.bookhub_back.entity.Employee;
import com.bookhub.bookhub_back.repository.DiscountPolicyRepository;
import com.bookhub.bookhub_back.repository.EmployeeRepository;
import com.bookhub.bookhub_back.service.AlertService;
import com.bookhub.bookhub_back.service.DiscountPolicyService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.tree.pattern.ParseTreePattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountPolicyServiceImpl implements DiscountPolicyService {
    private final DiscountPolicyRepository policyRepository;
    private final EmployeeRepository employeeRepository;
    private final AlertService alertService;

    // 할인 정책 등록
    @Override
    @Transactional
    public ResponseDto<Void> createPolicy(DiscountPolicyCreateRequestDto dto) {
        if (dto.getPolicyType() == PolicyType.TOTAL_PRICE_DISCOUNT){
            if(dto.getTotalPriceAchieve() == null) {
                throw new IllegalArgumentException("'총 금액' 항목을 입력해주세요.");
            }
        }else {
            if (dto.getTotalPriceAchieve() != null) {
                throw new IllegalArgumentException("'총 금액' 항목은 총 금액 할인에만 설정할 수 있습니다.");
            }
        }

        DiscountPolicy newPolicy = DiscountPolicy.builder()
                .policyTitle(dto.getPolicyTitle())
                .policyDescription(dto.getPolicyDescription())
                .policyType(dto.getPolicyType())
                .totalPriceAchieve(dto.getTotalPriceAchieve())
                .discountPercent(dto.getDiscountPercent())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        DiscountPolicy saved = policyRepository.save(newPolicy);

        // 알림 기능: 모든 직원에게 이벤트 생성 알림 보내기(NOTICE)
        for (Employee employee : employeeRepository.findAll()) {
            alertService.createAlert(AlertCreateRequestDto.builder()
                    .employeeId(employee.getEmployeeId())
                    .alertType(String.valueOf(AlertType.NOTICE))
                    .alertTargetTable("DISCOUNT_POLICIES")
                    .targetPk(saved.getPolicyId())
                    .message("새로운 할인 정책이 생성되었습니다.")
                    .build());
        }

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 할인 정책 수정
    @Override
    public ResponseDto<Void> updatePolicy(Long policyId, DiscountPolicyUpdateRequestDto dto) {
        DiscountPolicy policy = policyRepository.findById(policyId)
                .orElseThrow(EntityNotFoundException::new);

        if (dto.getTotalPriceAchieve() != null) {
            if (policy.getPolicyType() != PolicyType.TOTAL_PRICE_DISCOUNT) {
                throw new IllegalArgumentException("'총 금액' 항목은 총 금액 할인에만 설정할 수 있습니다.");
            }
            policy.setTotalPriceAchieve(dto.getTotalPriceAchieve());
            policy.setPolicyDescription(dto.getPolicyDescription());
            policy.setDiscountPercent(dto.getDiscountPercent());
            policy.setStartDate(dto.getStartDate());
            policy.setEndDate(dto.getEndDate());

            policyRepository.save(policy);

        }
            return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 할인 정책 삭제
    @Override
    public ResponseDto<Void> deletePolicy(Long policyId) {
        DiscountPolicy policy = policyRepository.findById(policyId)
                .orElseThrow(EntityNotFoundException::new);

        policyRepository.delete(policy);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    // 전체 조회
    @Override
    public ResponseDto<PageResponseDto<DiscountPolicyListResponseDto>> getPolicies(int page, int size, String keyword, PolicyType type, LocalDate start, LocalDate end) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DiscountPolicy> result = policyRepository.findFiltered(
                keyword != null && keyword.isBlank() ? null : keyword,
                type,
                start,
                end,
                pageable
        );

        List<DiscountPolicyListResponseDto> content = result.getContent().stream()
                .map(p -> DiscountPolicyListResponseDto.builder()
                        .policyId(p.getPolicyId())
                        .policyTitle(p.getPolicyTitle())
                        .policyType(p.getPolicyType())
                        .startDate(p.getStartDate())
                        .endDate(p.getEndDate())
                        .build()
                ).collect(Collectors.toList());

        PageResponseDto<DiscountPolicyListResponseDto> pageDto = PageResponseDto.of(
                content,
                result.getTotalElements(),
                result.getTotalPages(),
                result.getNumber()
        );

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, pageDto);
    }

    // 단건 조회
    @Override
    public ResponseDto<DiscountPolicyDetailResponseDto> getPolicyById(Long policyId) {

        DiscountPolicy policy = policyRepository.findById(policyId)
                .orElseThrow(EntityNotFoundException::new);

        DiscountPolicyDetailResponseDto responseDto = DiscountPolicyDetailResponseDto.builder()
                .policyTitle(policy.getPolicyTitle())
                .policyDescription(policy.getPolicyDescription())
                .policyType(policy.getPolicyType())
                .totalPriceAchieve(policy.getTotalPriceAchieve())
                .discountPercent(policy.getDiscountPercent())
                .startDate(policy.getStartDate())
                .endDate(policy.getEndDate())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

}
