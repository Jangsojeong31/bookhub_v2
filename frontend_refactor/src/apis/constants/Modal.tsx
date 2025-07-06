// src/components/common/Modal.tsx

// 사용방법

//현재 만들어져 있는 Modal은 껍데기
//그래서 해당 모달 안에 들어갈 내용을 .tsx파일에 정의하고(예시 CreatePublisher)
//해당 모달에서 실행할 내용을 index.tsx 파일에서 실행 (아래 코드는 index에서 불러올때 내용용)
//       {/* 등록 모달 */}
//    {isCreateOpen && ( 
//   <CreatePublisher-- 각자 만든 모달 내용을 정의한 페이지
//     isOpen={isCreateOpen}
//     onClose={() => setIsCreateOpen(false)}
//     onCreated={fetchPublishers}
//   />
// )}



import React, { useEffect } from 'react';
import styled from '@emotion/styled';
import ReactDOM from 'react-dom';

type ModalProps = {
  isOpen: boolean;
  onClose: () => void;
  children: React.ReactNode;
  className? : string;
  
};

const Modal = ({ isOpen, onClose, children }: ModalProps) => {
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') {
        onClose();
      }
    };
    if (isOpen) {
      document.addEventListener('keydown', handleKeyDown);
    }
    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [isOpen, onClose]);

  if (!isOpen) return null;

  return ReactDOM.createPortal(
    <Overlay onClick={onClose}>
      <Container onClick={(e) => e.stopPropagation()}>
        <CloseButton onClick={onClose}>×</CloseButton>
        {children}
      </Container>
    </Overlay>,
    document.getElementById('modal-root') as HTMLElement
  );
};

export default Modal;

const Overlay = styled.div`
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
`;

const Container = styled.div`
  background: white;
  border-radius: 12px;
  padding: 2rem;
  width: 1000px;
  height: 500px;
  max-width: 90%;
  max-height: 80%;
  position: relative;
`;

const CloseButton = styled.button`
  position: absolute;
  top: 8px; right: 12px;
  background: transparent;
  border: none;
  font-size: 24px;
  cursor: pointer;
`;

