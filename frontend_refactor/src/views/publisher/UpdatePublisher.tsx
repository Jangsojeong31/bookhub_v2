// src/views/publisher/UpdatePublisher.tsx
import React, { useState, useEffect } from 'react';
import Modal from '@/apis/constants/Modal';
import { PublisherResponseDto } from '@/dtos/publisher/response/publisher.response.dto';
import { PublisherRequestDto } from '@/dtos/publisher/request/publisher.request.dto';
import { updatePublisher } from '@/apis/publisher/publisher';
import { useCookies } from 'react-cookie';
import './publisher.css';

interface UpdatePublisherProps {
  publisher: PublisherResponseDto;
  onClose: () => void;
  onUpdated: () => void | Promise<void>;
}

const UpdatePublisher: React.FC<UpdatePublisherProps> = ({ publisher, onClose, onUpdated }) => {
  const [name, setName] = useState<string>(publisher.publisherName);
  const [message, setMessage] = useState<string>('');
  const [cookies] = useCookies(['accessToken']);

  useEffect(() => {
    // prop이 바뀔 때마다 동기화
    setName(publisher.publisherName);
    setMessage('');
  }, [publisher]);

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setName(e.target.value);
    setMessage('');
  };

  const onSubmit = async () => {
    const token = cookies.accessToken;
    if (!token) {
      alert('인증 토큰이 없습니다.');
      return;
    }
    const trimmed = name.trim();
    if (!trimmed) {
      setMessage('출판사 이름을 입력해주세요.');
      return;
    }
    const dto: PublisherRequestDto = { publisherName: trimmed };
    try {
      const response = await updatePublisher(publisher.publisherId, dto, token);
      const { code, message: respMsg } = response;
      if (code !== 'SU') {
        setMessage(respMsg || '수정 중 오류가 발생했습니다.');
        return;
      }
      alert(`출판사명이 "${trimmed}"(으)로 수정되었습니다.`);
      // 부모 콜백 호출
      await onUpdated();
      onClose();
    } catch (err) {
      console.error('수정 중 예외:', err);
      setMessage('알 수 없는 오류가 발생했습니다.');
    }
  };

  return (
    <Modal isOpen={true} onClose={onClose}>
      <div>
        <h3 className="publisher-modal-header">출판사 수정</h3>
        <input
          type="text"
          placeholder="출판사 이름"
          value={name}
          onChange={onInputChange}
          className="publisher-input"
        />
        {message && <p className="publisher-error-message">{message}</p>}
        <div className="publisher-button-group">
          <button className="button" onClick={onSubmit}>저장</button>
          <button className="button" onClick={onClose}>취소</button>
        </div>
      </div>
    </Modal>
  );
};

export default UpdatePublisher;

