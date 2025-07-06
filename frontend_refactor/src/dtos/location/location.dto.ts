import { DisplayType } from "@/apis/enums/DisplayType";

export interface LocationCreateRequestDto {
  bookIsbn: string;
  floor: string;
  hall: string;
  section: string;
  displayType: DisplayType;
  note?: string | null;
}

export interface LocationUpdateRequestDto {
  floor?: string;
  hall?: string;
  section?: string;
  displayType?: DisplayType;
  note?: string | null;
}

export interface LocationResponseDto {
  locationId: number;
  bookTitle: string;
  floor: string;
}

export interface LocationDetailResponseDto {
  locationId: number;
  bookTitle: string;
  floor: string;
  hall: string;
  section: string;
  type: DisplayType;
  note?: string | null;
}
