import React, { useState } from 'react';
import { createPublisher } from '@/apis/publisher/publisher';
import Modal from '@/apis/constants/Modal';
import { useCookies } from 'react-cookie';

interface Props {
  isOpen: boolean;
  onClose: () => void;
  onCreated: () => void; // 등록 후 부모에서 리스트 새로고침
}

function CreatePublisher({ isOpen, onClose, onCreated }: Props) {
  const [publisherName, setPublisherName] = useState('');
  const [message, setMessage] = useState('');
  const [cookies] = useCookies(['accessToken']);

  const onCreateClick = async () => {
    if (!publisherName.trim()) {
      setMessage('출판사 이름을 입력해주세요.');
      return;
    }

    const token = cookies.accessToken;
    if (!token) {
      alert('인증 토큰이 없습니다.');
      return;
    }

    const dto = { publisherName };

    const response = await createPublisher(dto, token);
    const { code, message } = response;

    if (!code) {
      setMessage(message);
      return;
    }

    alert('출판사 등록이 완료되었습니다.');
    onCreated();   // 부모로부터 받은 refresh trigger
    onClose();     // 모달 닫기
    setPublisherName('');
    setMessage('');
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose}>
      <div style={{ padding: '10px 20px', textAlign: 'center' }}>
        <h2 style={{ marginBottom: '16px' }}>출판사 이름 등록</h2>
        <input
          type="text"
          placeholder="출판사 이름"
          value={publisherName}
          onChange={(e) => setPublisherName(e.target.value)}
          style={{
            width: '100%',
            padding: '10px',
            border: '1px solid #ccc',
            borderRadius: '4px',
            marginBottom: '12px',
          }}
        />
        <button
          onClick={onCreateClick}
          style={{
            width: '100%',
            padding: '10px',
            backgroundColor: '#4e7fff',
            color: 'white',
            border: 'none',
            borderRadius: '6px',
            fontSize: '16px',
            cursor: 'pointer'
          }}
        >
          등록
        </button>
        {message && (
          <p style={{ color: 'red', marginTop: '10px' }}>{message}</p>
        )}
      </div>
    </Modal>
  );
}

export default CreatePublisher;
