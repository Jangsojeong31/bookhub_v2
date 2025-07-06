// src/views/publisher/CreatePublisherModalLauncher.tsx
import React, { useState } from 'react';
import { PublisherRequestDto } from '@/dtos/publisher/request/publisher.request.dto';
import { createPublisher } from '@/apis/publisher/publisher';
import { useCookies } from 'react-cookie';
import Modal from '@/apis/constants/Modal';
import './publisher.css';

interface CreatePublisherModalLauncherProps {
  onCreated?: () => void | Promise<void>; // 등록 성공 후 목록 재조회 콜백
}

const CreatePublisherModalLauncher: React.FC<CreatePublisherModalLauncherProps> = ({ onCreated }) => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [publisherName, setPublisherName] = useState('');
  const [message, setMessage] = useState('');
  const [cookies] = useCookies(['accessToken']);

  const openModal = () => {
    setIsModalOpen(true);
    setPublisherName('');
    setMessage('');
  };
  const closeModal = () => {
    setIsModalOpen(false);
    setPublisherName('');
    setMessage('');
  };

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPublisherName(e.target.value);
    setMessage('');
  };

  const onSubmit = async () => {
    const token = cookies.accessToken;
    if (!token) {
      alert('인증 토큰이 없습니다.');
      return;
    }
    const nameTrimmed = publisherName.trim();
    if (!nameTrimmed) {
      setMessage('출판사 이름을 입력해주세요.');
      return;
    }
    const dto: PublisherRequestDto = { publisherName: nameTrimmed };
    try {
      const response = await createPublisher(dto, token);
      const { code, message: respMsg } = response;
      if (code !== 'SU') {
        setMessage(respMsg || '등록 중 오류가 발생했습니다.');
        return;
      }
      alert(`출판사 “${nameTrimmed}”이(가) 등록되었습니다.`);
      // 콜백 호출: 목록 재조회
      if (onCreated) {
        try {
          await onCreated();
        } catch (err) {
          console.error('onCreated 중 오류:', err);
        }
      }
      closeModal();
    } catch (err) {
      console.error('등록 중 예외:', err);
      setMessage('알 수 없는 오류가 발생했습니다.');
    }
  };

  return (
    <>
      <button className="button" onClick={openModal}>출판사 등록</button>
      <Modal isOpen={isModalOpen} onClose={closeModal}>
        <div>
          <h3 className="publisher-modal-header">출판사 등록</h3>
          <input
            type="text"
            placeholder="출판사 이름"
            value={publisherName}
            onChange={onInputChange}
            className="publisher-input"
          />
          {message && <p className="publisher-error-message">{message}</p>}
          <div className="publisher-button-group">
            <button className="button" onClick={onSubmit}>등록</button>
            <button className="button" onClick={closeModal}>취소</button>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default CreatePublisherModalLauncher;
