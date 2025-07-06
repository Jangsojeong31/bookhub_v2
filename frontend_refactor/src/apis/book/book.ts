import { ResponseDto } from "@/dtos";
import { BookCreateRequestDto } from "@/dtos/book/request/book-create.request.dto";
import { BookResponseDto } from "@/dtos/book/response/book-response.dto";
import { axiosInstance, bearerAuthorization, responseErrorHandler, responseSuccessHandler } from "../axiosConfig";
import { AxiosError } from "axios";
import { GET_BOOK_URL, POST_BOOK_URL, HIDE_BOOK_URL, UPDATE_BOOK_URL } from "../constants/sjw.constants";
import { BookUpdateRequestDto } from "@/dtos/book/request/book-update.request.dto";
import { BookLogResponseDto } from "@/dtos/book/response/book-log-response.dto";

// 책 등록
export const createBook = async (
  dto: BookCreateRequestDto,
  accessToken: string,
  file: File | null
): Promise<ResponseDto<BookResponseDto>> => {
  try {
    const formData = new FormData();
    formData.append(
      "dto",
      new Blob([JSON.stringify(dto)], { type: "application/json" })
    );
    if (file) {
      formData.append("coverImageFile", file);
    }

    const response = await axiosInstance.post(
      POST_BOOK_URL,
      formData,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};

// 책 수정
export const updateBook = async (
  isbn: string,
  dto: BookUpdateRequestDto,
  accessToken: string,
  coverFile: File | null
): Promise<ResponseDto<BookResponseDto>> => {
  const formData = new FormData();
  const blob = new Blob([JSON.stringify(dto)], { type: "application/json" });

  formData.append("dto", blob);
  if (coverFile) formData.append("file", coverFile);

  const response = await axiosInstance.put(
    UPDATE_BOOK_URL(isbn),
    formData,
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
        "Content-Type": "multipart/form-data",
      },
    }
  );

  return responseSuccessHandler(response);
};


// 책 삭제(HIDDEN 처리)
export const hideBook = async (
  isbn: string,
  accessToken: string
): Promise<ResponseDto<null>> => {
  try {
    const response = await axiosInstance.put(
      HIDE_BOOK_URL(isbn),
      null,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};



export const searchBook = async (
  keyword: string,
  accessToken: string
): Promise<ResponseDto<BookResponseDto[]>> => {
  try {
    const response = await axiosInstance.get(
      `${GET_BOOK_URL}?keyword=${keyword}`,
      bearerAuthorization(accessToken)
    );
    return responseSuccessHandler(response);
  } catch (error) {
    return responseErrorHandler(error as AxiosError<ResponseDto>);
  }
};


export const getBookByIsbn = async (
  isbn: string,
  accessToken: string
): Promise<ResponseDto<BookResponseDto | null>> => {
  const res = await searchBook(isbn.trim(), accessToken);

  if (res.code !== "SU" || !res.data) {
    return { code: "FA", message: "검색 실패", data: null };
  }

  const found = res.data.find(book => book.isbn.trim() === isbn.trim());

  if (!found) {
    return { code: "FA", message: "해당 ISBN의 책이 없습니다", data: null };
  }

  return { code: "SU", message: "성공", data: found };
};

// 책 로그 조회
export const getBookLogs = async (
  isbn: string,
  accessToken: string
): Promise<ResponseDto<BookLogResponseDto[]>> => {
  const response = await axiosInstance.get(`/api/v1/admin/book-logs/${isbn}`, {
  headers: {
    Authorization: `Bearer ${accessToken}`,
  },
});
  return response.data;
};






