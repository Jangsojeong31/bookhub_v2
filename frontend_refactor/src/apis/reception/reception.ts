import axios from "axios";
import { ResponseDto } from "@/dtos";
import { ReceptionListResponseDto } from "@/dtos/reception/response/receptionlist-response.dto";
import { GET_ADMIN_RECEPTION_URL, GET_CONFIRMED_RECEPTION_URL, GET_PENDING_RECEPTION_URL, PUT_RECEPTION_URL } from "../constants/sjw.constants";

export const getAllReceptionApproval = async (
  token: string
): Promise<ResponseDto<ReceptionListResponseDto[]>> => {
  const response = await axios.get<ResponseDto<ReceptionListResponseDto[]>>(
    GET_CONFIRMED_RECEPTION_URL,
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
};

export const getAllPendingReception = async (
  token: string,
): Promise<ResponseDto<ReceptionListResponseDto[]>> => {
  const response = await axios.get<ResponseDto<ReceptionListResponseDto[]>>(
    GET_PENDING_RECEPTION_URL,
    {
      headers: {
        AUthorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
}

export const putReception = async (
  token: string,
  purchaseOrderApprovalId: number
): Promise<ResponseDto<ReceptionListResponseDto[]>> => {
  const response = await axios.put<ResponseDto<ReceptionListResponseDto[]>>(
    PUT_RECEPTION_URL(purchaseOrderApprovalId),{},
    {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );
  return response.data;
}

export const getAdminReceptionApproval = async (
  token: string,
  branchName?: string,
  bookIsbn?: string
): Promise<ResponseDto<ReceptionListResponseDto[]>> => {
  let url = GET_ADMIN_RECEPTION_URL;
  const params = new URLSearchParams();

  if (branchName) params.append("branchName", branchName);
  if (bookIsbn) params.append("bookIsbn", bookIsbn);

  if ([...params].length > 0) {
    url += `?${params.toString()}`;
  }

  const response = await axios.get<ResponseDto<ReceptionListResponseDto[]>>(url, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response.data;
};
