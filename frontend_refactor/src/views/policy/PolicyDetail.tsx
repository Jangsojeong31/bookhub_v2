import React from 'react';
import { PolicyDetailResponseDto } from '@/dtos/policy/policy.response.dto';
import './policyC.css';

interface PolicyDetailProps {
  isOpen: boolean;
  onClose: () => void;
  policyDetail: PolicyDetailResponseDto;
  policyId: number;
}

const PolicyDetail: React.FC<PolicyDetailProps> = ({ isOpen, onClose, policyDetail, policyId }) => {
  if (!isOpen) return null;

  return (
    <div className="modal-overlay">
      <div className="policy-detail-modal">
        <h2 className="modal-title">정책 상세 조회</h2>
        <table className="detail-table">
          <tbody>
            <tr>
              <th>정책 ID</th>
              <td>{policyId}</td>
            </tr>
            <tr>
              <th>제목</th>
              <td>{policyDetail.policyTitle}</td>
            </tr>
            <tr>
              <th>기간</th>
              <td>{policyDetail.startDate} - {policyDetail.endDate}</td>
            </tr>
            {/* 총 구매 금액 기준이 필요한 경우 표시 */}
            {policyDetail.totalPriceAchieve != null && (
              <tr>
                <th>기준 금액</th>
                <td>{policyDetail.totalPriceAchieve.toLocaleString()}원</td>
              </tr>
            )}
            <tr>
              <th>할인율</th>
              <td>{policyDetail.discountPercent}%</td>
            </tr>
            <tr>
              <th>타입</th>
              <td>{policyDetail.policyType}</td>
            </tr>
            <tr>
              <th>설명</th>
              <td>{policyDetail.policyDescription || '-'}</td>
            </tr>
          </tbody>
        </table>
        <div className="modal-footer">
          <button className="btn-primary" onClick={onClose}>목록으로</button>
        </div>
      </div>
    </div>
  );
};

export default PolicyDetail;

