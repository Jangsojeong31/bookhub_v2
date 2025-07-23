package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.util.DateUtils;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.branch.request.BranchRequestDto;
import com.bookhub.bookhub_back.dto.branch.response.BranchResponseDto;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.service.BranchService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;

    @Override
    public ResponseDto<BranchResponseDto> createBranch(BranchRequestDto dto) {
        if (branchRepository.existsByBranchName(dto.getBranchName())) {
            throw new DuplicateEntityException("이미 존재하는 지점입니다.");
        }

        Branch newBranch = Branch.builder()
                .branchName(dto.getBranchName())
                .branchLocation(dto.getBranchLocation())
                .build();

        Branch savedBranch = branchRepository.save(newBranch);

        BranchResponseDto responseDto = toResponseDto(savedBranch);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<BranchResponseDto>> getBranchesByLocation(String branchLocation) {

        List<Branch> branches = branchLocation == null || branchLocation.isBlank()
                ? branchRepository.findAll()
                : branchRepository.findByBranchLocationContaining(branchLocation);

        List<BranchResponseDto> responseDtos = branches.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<BranchResponseDto> getBranchById(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(EntityNotFoundException::new);

        BranchResponseDto responseDto = toResponseDto(branch);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<BranchResponseDto> updateBranch(Long branchId, BranchRequestDto dto) {

        boolean isBranchExists = branchRepository.existsByBranchNameAndBranchIdNot(dto.getBranchName(), branchId);
        if (isBranchExists) {
            throw new DuplicateEntityException("이미 존재하는 지점입니다.");
        }

        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(EntityNotFoundException::new);

        branch.setBranchName(dto.getBranchName());
        branch.setBranchLocation(dto.getBranchLocation());

        Branch updatedBranch = branchRepository.save(branch);

        BranchResponseDto responseDto = toResponseDto(updatedBranch);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    // responseDto 변환 메서드
    private BranchResponseDto toResponseDto(Branch branch) {
        return BranchResponseDto.builder()
                .branchId(branch.getBranchId())
                .branchName(branch.getBranchName())
                .branchLocation(branch.getBranchLocation())
                .createdAt(DateUtils.format(branch.getCreatedAt()))
                .updatedAt(DateUtils.format(branch.getUpdatedAt()))
                .build();
    }

}
