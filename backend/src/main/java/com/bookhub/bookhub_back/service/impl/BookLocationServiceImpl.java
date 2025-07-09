package com.bookhub.bookhub_back.service.impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.location.request.LocationCreateRequestDto;
import com.bookhub.bookhub_back.dto.location.request.LocationUpdateRequestDto;
import com.bookhub.bookhub_back.dto.location.response.LocationCreateResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationDetailResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationResponseDto;
import com.bookhub.bookhub_back.dto.location.response.LocationUpdateResponseDto;
import com.bookhub.bookhub_back.entity.BookDisplayLocation;
import com.bookhub.bookhub_back.entity.Book;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.repository.BookDisplayLocationRepository;
import com.bookhub.bookhub_back.repository.BookRepository;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.service.BookLocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLocationServiceImpl implements BookLocationService {
    private final BranchRepository branchRepository;
    private final BookRepository bookRepository;
    private final BookDisplayLocationRepository bookLocationRepository;

    @Override
    public ResponseDto<LocationCreateResponseDto> createLocation(Long branchId, LocationCreateRequestDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        Book book = bookRepository.findById(dto.getBookIsbn())
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        BookDisplayLocation newLocation = BookDisplayLocation.builder()
                .branchId(branch)
                .bookIsbn(book)
                .floor(dto.getFloor())
                .hall(dto.getHall())
                .section(dto.getSection())
                .displayType(dto.getDisplayType())
                .note(dto.getNote())
                .build();

        BookDisplayLocation saved = bookLocationRepository.save(newLocation);

        LocationCreateResponseDto responseDto = LocationCreateResponseDto.builder()
                .bookTitle(saved.getBookIsbn().getBookTitle())
                .floor(saved.getFloor())
                .hall(saved.getHall())
                .section(saved.getSection())
                .type(saved.getDisplayType())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<LocationUpdateResponseDto> updateLocation(Long branchId, Long locationId, LocationUpdateRequestDto dto) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        if(dto.getFloor() != null) location.setFloor(dto.getFloor());
        if(dto.getHall() != null) location.setHall(dto.getHall());
        if(dto.getSection() != null) location.setSection(dto.getSection());
        if(dto.getDisplayType() != null) location.setDisplayType(dto.getDisplayType());
        if(dto.getNote() != null) location.setNote(dto.getNote());

        BookDisplayLocation updatedLocation = bookLocationRepository.save(location);

        LocationUpdateResponseDto responseDto = LocationUpdateResponseDto.builder()
                .bookTitle(updatedLocation.getBookIsbn().getBookTitle())
                .floor(updatedLocation.getFloor())
                .hall(updatedLocation.getHall())
                .section(updatedLocation.getSection())
                .type(updatedLocation.getDisplayType())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<List<LocationResponseDto>> searchBranchBooksByTitle(Long branchId, String keyword) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID + ResponseMessage.NO_EXIST_ID));

        List<Book> books = bookRepository.findAllByBookTitleContaining(keyword);

        if (books.isEmpty()) {
            throw new EntityNotFoundException(ResponseCode.NO_EXIST_ID + ResponseMessage.NO_EXIST_ID);
        }

        List<BookDisplayLocation> locations = bookLocationRepository.findByBranchAndBooks(branch, books);

        if (locations.isEmpty()) {
            throw new EntityNotFoundException(ResponseCode.NO_EXIST_ID + ": 해당 지점에 해당 책들이 진열되어 있지 않습니다.");
        }

        List<LocationResponseDto> responseDtos = locations.stream()
                .map(location -> LocationResponseDto.builder()
                        .locationId(location.getLocationId())
                        .bookTitle(location.getBookIsbn().getBookTitle())
                        .floor(location.getFloor())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<LocationDetailResponseDto> getLocation(Long branchId, Long locationId) {
        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        LocationDetailResponseDto responseDto = LocationDetailResponseDto.builder()
                .locationId(location.getLocationId())
                .bookTitle(location.getBookIsbn().getBookTitle())
                .floor(location.getFloor())
                .hall(location.getHall())
                .section(location.getSection())
                .type(location.getDisplayType())
                .note(location.getNote())
                .build();

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDto);
    }

    @Override
    public ResponseDto<Void> deleteLocation(Long branchId, Long locationId) {
        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        bookLocationRepository.delete(location);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

}
