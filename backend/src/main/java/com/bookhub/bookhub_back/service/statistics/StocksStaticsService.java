package com.bookhub.bookhub_back.service.statistics;

import com.bookhub.bookhub_back.dto.ResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.BranchStockBarChartDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.CategoryStockResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.TimeStockChartResponseDto;
import com.bookhub.bookhub_back.dto.statistics.response.stocks.ZeroStockResponseDto;

import java.util.List;

public interface StocksStaticsService {

    ResponseDto<List<BranchStockBarChartDto>> getBranchStockBarChart(int year, int month);

    ResponseDto<List<TimeStockChartResponseDto>> getTimeStockStatistics(int year);

    ResponseDto<List<ZeroStockResponseDto>> getZeroStockBooks();

    ResponseDto<List<CategoryStockResponseDto>> getCategoryStocks(String branchName);
}
