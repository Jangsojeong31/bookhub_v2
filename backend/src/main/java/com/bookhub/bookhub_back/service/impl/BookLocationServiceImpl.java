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
import com.bookhub.bookhub_back.exception.DuplicateEntityException;
import com.bookhub.bookhub_back.repository.BookDisplayLocationRepository;
import com.bookhub.bookhub_back.repository.BookRepository;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.security.UserPrincipal;
import com.bookhub.bookhub_back.service.BookLocationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.internal.LoadingCache;
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
    public ResponseDto<LocationCreateResponseDto> createLocation(UserPrincipal userPrincipal, LocationCreateRequestDto dto) {
        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        Book book = bookRepository.findByIdNotHidden(dto.getBookIsbn())
                .orElseThrow(EntityNotFoundException::new);

        if(bookLocationRepository.existsByBookIsbn_BookIsbnAndFloorAndHallAndSectionAndDisplayType(
                dto.getBookIsbn(), dto.getFloor(), dto.getHall(), dto.getSection(), dto.getDisplayType())) {
            throw new DuplicateEntityException("해당 조건의 책 위치가 이미 존재합니다.");
        }

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
    public ResponseDto<LocationUpdateResponseDto> updateLocation(Long locationId, LocationUpdateRequestDto dto) {

        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException(ResponseCode.NO_EXIST_ID));

        if(bookLocationRepository.existsByBookIsbn_BookIsbnAndFloorAndHallAndSectionAndDisplayType(
                location.getBookIsbn().getBookIsbn(), dto.getFloor(), dto.getHall(), dto.getSection(), dto.getDisplayType())) {
            throw new DuplicateEntityException("해당 조건의 책 위치가 이미 존재합니다.");
        }

        location.setFloor(dto.getFloor());
        location.setHall(dto.getHall());
        location.setSection(dto.getSection());
        location.setDisplayType(dto.getDisplayType());
        location.setNote(dto.getNote());

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
    public ResponseDto<List<LocationResponseDto>> searchBranchBooksByTitle(UserPrincipal userPrincipal, String keyword) {
        Branch branch = branchRepository.findById(userPrincipal.getBranchId())
                .orElseThrow(EntityNotFoundException::new);

        List<Book> books = bookRepository.findAllByBookTitleContaining(keyword);

        if (books.isEmpty()) {
            throw new IllegalArgumentException("검색한 도서가 존재하지 않습니다.");
        }

        List<BookDisplayLocation> locations = bookLocationRepository.findByBranchAndBooks(branch, books);

        List<LocationResponseDto> responseDtos = locations.stream()
                .map(location -> LocationResponseDto.builder()
                        .locationId(location.getLocationId())
                        .bookTitle(location.getBookIsbn().getBookTitle())
                        .floor(location.getFloor())
                        .hall(location.getHall())
                        .section(location.getSection())
                        .displayType(location.getDisplayType())
                        .note(location.getNote())
                        .build())
                .collect(Collectors.toList());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    @Override
    public ResponseDto<LocationDetailResponseDto> getLocation(Long locationId) {
        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(EntityNotFoundException::new);

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
    public ResponseDto<Void> deleteLocation(Long locationId) {
        BookDisplayLocation location = bookLocationRepository.findById(locationId)
                .orElseThrow(EntityNotFoundException::new);

        bookLocationRepository.delete(location);

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

}
