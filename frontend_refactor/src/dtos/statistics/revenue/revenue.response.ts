export interface WeekdayRevenueResponseDto{
  weekday : string;
  totalRevenue : number;
}

export interface BranchRevenueResponseDto{
  branchId : number;
  branchName : string;
  categoryName : string;
  totalRevenue : number;
}

export interface MonthlyRevenueResponseDto{
  orderedMonth : number;
  totalRevenue : number;
}

export interface WeeklyRevenueResponseDto{
  // branchId: number;
  // branchName : string;
  // weekIndex : number;
  // weekStartDate : Date;
  totalRevenue: number;
  orderedDate: string;
}