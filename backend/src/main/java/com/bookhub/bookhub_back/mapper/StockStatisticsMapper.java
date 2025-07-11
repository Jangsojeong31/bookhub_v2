package com.bookhub.bookhub_back.mapper;

import com.bookhub.bookhub_back.common.enums.StockActionType;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.CategoryStockResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.TimeStockChartResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.ZeroStockResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StockStatisticsMapper {
    Long findTotalAmountByBranchAndType(Long branchId, StockActionType type, int year, int month);

    List<CategoryStockResponseDto> findCategoryStockByBranch(String branchName);

    List<ZeroStockResponseDto> findZeroStockStatistics();

    List<TimeStockChartResponseDto> findTimeStockStatisticsByYear(int year);
}
