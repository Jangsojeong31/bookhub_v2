package com.bookhub.bookhub_back.service.statistics.Impl;

import com.bookhub.bookhub_back.common.constants.ResponseCode;
import com.bookhub.bookhub_back.common.constants.ResponseMessage;
import com.bookhub.bookhub_back.common.constants.ResponseMessageKorean;
import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.BranchStockBarChartDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.CategoryStockResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.TimeStockChartResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.ZeroStockResponseDto;
import com.bookhub.bookhub_back.entity.Branch;
import com.bookhub.bookhub_back.mapper.StockStatisticsMapper;
import com.bookhub.bookhub_back.repository.BranchRepository;
import com.bookhub.bookhub_back.repository.statistics.StocksStatisticsRepository;
import com.bookhub.bookhub_back.service.statistics.StocksStaticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockStatisticsServiceImpl implements StocksStaticsService {
    private final StockStatisticsMapper stockStatisticsMapper;
    private final BranchRepository branchRepository;

    @Override
    public ResponseDto<List<BranchStockBarChartDto>> getBranchStockBarChart(int year, int month) {
        List<Branch> branches = branchRepository.findAll();
        List<BranchStockBarChartDto> resultList = new ArrayList<>();

        long totalIn = 0L;
        long totalOut = 0L;
        long totalLoss = 0L;

        for (Branch branch : branches) {
            Long in = stockStatisticsMapper.findTotalAmountByBranchAndType(branch.getBranchId(), StockActionType.IN, year, month);
            Long out = stockStatisticsMapper.findTotalAmountByBranchAndType(branch.getBranchId(), StockActionType.OUT, year, month);
            Long loss = stockStatisticsMapper.findTotalAmountByBranchAndType(branch.getBranchId(), StockActionType.LOSS, year, month);

            in = in == null ? 0 : in;
            out = out == null ? 0 : out;
            loss = loss == null ? 0 : loss;

            resultList.add(BranchStockBarChartDto.builder()
                    .branchName(branch.getBranchName())
                    .inAmount(in)
                    .outAmount(out)
                    .lossAmount(loss)
                    .build());

            totalIn += in;
            totalOut += out;
            totalLoss += loss;
        }

        resultList.add(BranchStockBarChartDto.builder()
                .branchName("전체 합계")
                .inAmount(totalIn)
                .outAmount(totalOut)
                .lossAmount(totalLoss)
                .build());

        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, resultList);
    }

    @Override
    public ResponseDto<List<TimeStockChartResponseDto>> getTimeStockStatistics(int year) {
        List<TimeStockChartResponseDto> responseDtos = stockStatisticsMapper.findTimeStockStatisticsByYear(year);

        int currentMonth = (LocalDate.now().getYear() == year ? LocalDate.now().getMonthValue() : 12);

        List<Branch> allBranches = branchRepository.findAll();

        List<TimeStockChartResponseDto> result = new ArrayList<>();

        for (Branch branch : allBranches) {
            for (int month = 1; month <= currentMonth; month++) {
                int finalMonth = month;
                TimeStockChartResponseDto dto = responseDtos.stream()
                        .filter(d -> d.getBranchName().equals(branch.getBranchName())
                        && d.getMonth() == finalMonth)
                        .findFirst()
                        .orElse(null);

                if (dto != null && dto.getInAmount() != null && dto.getLossAmount() != null) {
                    result.add(TimeStockChartResponseDto.builder()
                                    .branchName(branch.getBranchName())
                                    .month(month)
                                    .inAmount(dto.getInAmount())
                                    .lossAmount(dto.getLossAmount())
                            .build());

                }
            }
        }
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessageKorean.SUCCESS, result);

    }

    @Override
    public ResponseDto<List<ZeroStockResponseDto>> getZeroStockBooks() {
        List<ZeroStockResponseDto> responseDtos = stockStatisticsMapper.findZeroStockStatistics();
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, responseDtos);
    }

    //상위 10개의 카테고리만 보여주고 나머지는 기타로 표시
    @Override
    public ResponseDto<List<CategoryStockResponseDto>> getCategoryStocks(String branchName) {
        List<CategoryStockResponseDto> dtos = stockStatisticsMapper.findCategoryStockByBranch(branchName);
        List<CategoryStockResponseDto> top10 = new ArrayList<>();
        long etcSum = 0L;

        for (int i = 0; i < dtos.size(); i++) {
            CategoryStockResponseDto dto = dtos.get(i);
            if (i < 9) {
                top10.add(new CategoryStockResponseDto(dto.getCategoryName(), dto.getTotalAmount()));
            } else {
                etcSum += dto.getTotalAmount();
            }
        }

        if (etcSum > 0) {
            top10.add(new CategoryStockResponseDto("기타", etcSum));
        }
        return ResponseDto.success(ResponseCode.SUCCESS, ResponseMessage.SUCCESS, top10);
    }
}
