import React, { useState } from 'react'
import { useCookies } from 'react-cookie'
import { useNavigate } from 'react-router-dom'
import { useEmployeeStore } from '@/stores/employee.store'
import './StockLogPage.css'

const StockLogPage = () => {
  const [cookies] = useCookies(['accessToken'])
  const navigate = useNavigate()
  const employee = useEmployeeStore((state) => state.employee)

  const branchId = employee?.branchId ?? null
  const employeeId = employee?.employeeId ?? null

  const [showIsbnModal, setShowIsbnModal] = useState(false)
  const [isbnInput, setIsbnInput] = useState('')

  const [showDateModal, setShowDateModal] = useState(false)
  const [startDate, setStartDate] = useState('')
  const [endDate, setEndDate] = useState('')

  if (!branchId || !employeeId) {
    return <div>지점 정보 불러오는 중...</div>
  }

  const handleIsbnConfirm = () => {
    if (!isbnInput.trim()) return
    navigate(`/stock-logs/branch/${branchId}/book/${isbnInput.trim()}`)
    setShowIsbnModal(false)
    setIsbnInput('')
  }

  const handleDateConfirm = () => {
    if (!startDate || !endDate) return
    navigate(`/stock-logs/branch/${branchId}/date?start=${startDate}T00:00:00&end=${endDate}T23:59:59`)
    setShowDateModal(false)
    setStartDate('')
    setEndDate('')
  }

  return (
    <div style={{ padding: '24px' }}>
      <h2 style={{ marginBottom: '24px' }}>재고 로그 메인</h2>
      <div className="button-group">
        <button className="stock-button" onClick={() => navigate(`/stock-logs/branch/${branchId}`)}>지점재고로그 전체 조회</button>
        <button className="stock-button" onClick={() => navigate(`/stock-logs/branch/${branchId}/type`)}>유형별 조회</button>
        <button className="stock-button" onClick={() => setShowDateModal(true)}>날짜별 조회</button>
        <button className="stock-button" onClick={() => navigate(`/stock-logs/employee/${employeeId}`)}>나의 재고로그 조회</button>
        <button className="stock-button" onClick={() => setShowIsbnModal(true)}>도서별 조회</button>
      </div>

      {/* 도서별 모달 */}
      {showIsbnModal && (
        <div style={modalBackdropStyle}>
          <div style={modalStyle}>
            <h3 style={{ marginBottom: '12px' }}>ISBN 입력</h3>
            <input
              type="text"
              value={isbnInput}
              onChange={(e) => setIsbnInput(e.target.value)}
              placeholder="예: 9781234567890"
              style={inputStyle}
            />
            <div style={modalButtonWrapperStyle}>
              <button onClick={handleIsbnConfirm} style={confirmButtonStyle}>확인</button>
              <button onClick={() => setShowIsbnModal(false)} style={cancelButtonStyle}>취소</button>
            </div>
          </div>
        </div>
      )}

      {/* 날짜별 모달 */}
      {showDateModal && (
        <div style={modalBackdropStyle}>
          <div style={modalStyle}>
            <h3 style={{ marginBottom: '12px' }}>조회할 날짜 범위 선택</h3>
            <label style={{ display: 'block', marginBottom: '8px' }}>
              시작일:
              <input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                style={{ ...inputStyle, marginTop: '4px' }}
              />
            </label>
            <label style={{ display: 'block', marginBottom: '16px' }}>
              종료일:
              <input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                style={{ ...inputStyle, marginTop: '4px' }}
              />
            </label>
            <div style={modalButtonWrapperStyle}>
              <button onClick={handleDateConfirm} style={confirmButtonStyle}>확인</button>
              <button onClick={() => setShowDateModal(false)} style={cancelButtonStyle}>취소</button>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}

export default StockLogPage

// ✅ 스타일 객체
const modalBackdropStyle: React.CSSProperties = {
  position: 'fixed',
  top: 0,
  left: 0,
  width: '100vw',
  height: '100vh',
  backgroundColor: 'rgba(0, 0, 0, 0.4)',
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  zIndex: 9999,
}

const modalStyle: React.CSSProperties = {
  backgroundColor: '#fff',
  padding: '24px',
  borderRadius: '8px',
  width: '600px',
  boxShadow: '0 4px 12px rgba(0, 0, 0, 0.2)',
}

const inputStyle: React.CSSProperties = {
  width: '100%',
  padding: '8px',
  fontSize: '14px',
  border: '1px solid #ccc',
  borderRadius: '4px',
  marginBottom: '8px',
}

const modalButtonWrapperStyle: React.CSSProperties = {
  display: 'flex',
  justifyContent: 'flex-end',
  gap: '8px',
}

const confirmButtonStyle: React.CSSProperties = {
  padding: '6px 12px',
  backgroundColor: '#007bff',
  color: 'white',
  border: 'none',
  borderRadius: '4px',
  cursor: 'pointer',
}

const cancelButtonStyle: React.CSSProperties = {
  padding: '6px 12px',
  backgroundColor: '#ccc',
  color: 'black',
  border: 'none',
  borderRadius: '4px',
  cursor: 'pointer',
}

