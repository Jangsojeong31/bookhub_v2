import React, { useEffect, useState } from "react";
import {
  getUnreadAlerts,
  markAlertsAsRead,
  getAlertTargetUrl,
} from "@/apis/alert/alert";
import { useCookies } from "react-cookie";
import { useEmployeeStore } from "@/stores/employee.store";
import { AlertResponseDto } from "@/dtos/alert/response/alert.response.dto";
import { Link } from "react-router-dom";
import "./AlertPage.css";

function AlertPage() {
  const [alerts, setAlerts] = useState<AlertResponseDto[]>([]);
  const [selectedAlerts, setSelectedAlerts] = useState<number[]>([]);
  const [cookies] = useCookies(["accessToken"]);
  const [currentPage, setCurrentPage] = useState(0);
  const itemsPerPage = 10;

  const employee = useEmployeeStore((state) => state.employee);

  const fetchAlerts = async () => {
    if (!employee) return;
    const res = await getUnreadAlerts(employee.employeeId, cookies.accessToken);
    if (res.code === "SU" && res.data) {
      setAlerts(res.data);
    }
  };

  useEffect(() => {
    fetchAlerts();
  }, [employee]);

  const handleCheckboxChange = (alertId: number) => {
    setSelectedAlerts((prev) =>
      prev.includes(alertId)
        ? prev.filter((id) => id !== alertId)
        : [...prev, alertId]
    );
  };

  const handleSelectAll = () => {
    if (selectedAlerts.length === alerts.length) {
      setSelectedAlerts([]);
    } else {
      setSelectedAlerts(alerts.map((a) => a.alertId));
    }
  };

  const handleMarkAsRead = async () => {
    if (selectedAlerts.length === 0) return;
    const res = await markAlertsAsRead(selectedAlerts, cookies.accessToken);
    if (res.code === "SU") {
      window.location.reload();
    }
  };

  const totalPages = Math.ceil(alerts.length / itemsPerPage);

  const goToPage = (page: number) => {
    if (page >= 0 && page < totalPages) {
      setCurrentPage(page);
    }
  };

  const alertsToDisplay = alerts.slice(
    currentPage * itemsPerPage,
    (currentPage + 1) * itemsPerPage
  );

  return (
    <div style={{ padding: "24px" }}>
      <h2>알림 목록</h2>

      <button
        className="read-btn"
        onClick={handleMarkAsRead}
        disabled={selectedAlerts.length === 0}
      >
        읽음
      </button>

      <table style={{ width: "100%", marginTop: "16px", borderCollapse: "collapse" }}>
        <thead>
          <tr>
            <th>
              <input
                type="checkbox"
                checked={selectedAlerts.length === alerts.length && alerts.length > 0}
                onChange={handleSelectAll}
              />
            </th>
            <th>알림 타입</th>
            <th>메시지</th>
            <th>읽음 여부</th>
            <th>수신일</th>
          </tr>
        </thead>
        <tbody>
          {alertsToDisplay.map((alert) => (
            <tr key={alert.alertId}>
              <td>
                <input
                  type="checkbox"
                  checked={selectedAlerts.includes(alert.alertId)}
                  onChange={() => handleCheckboxChange(alert.alertId)}
                />
              </td>
              <td>{alert.alertType}</td>
              <td style={{ fontWeight: alert.isRead ? "normal" : "bold" }}>
                {getAlertTargetUrl(alert) ? (
                  <Link
                    to={getAlertTargetUrl(alert)!}
                    style={{ color: "#004080", textDecoration: "underline" }}
                  >
                    {alert.message}
                  </Link>
                ) : (
                  alert.message
                )}
              </td>
              <td>{alert.isRead ? "읽음" : "안읽음"}</td>
              <td>{new Date(alert.createdAt).toLocaleDateString("ko-KR")}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {alerts.length > 0 && (
        <div className="footer">
          <button className="pageBtn" onClick={() => goToPage(currentPage - 1)} disabled={currentPage === 0}>
            {"<"}
          </button>
          {Array.from({ length: totalPages }, (_, i) => (
            <button
              key={i}
              className={`pageBtn${i === currentPage ? " current" : ""}`}
              onClick={() => goToPage(i)}
            >
              {i + 1}
            </button>
          ))}
          <button
            className="pageBtn"
            onClick={() => goToPage(currentPage + 1)}
            disabled={currentPage >= totalPages - 1}
          >
            {">"}
          </button>
          <span className="pageText">
            {totalPages > 0 ? `${currentPage + 1} / ${totalPages}` : "0 / 0"}
          </span>
        </div>
      )}
    </div>
  );
}

export default AlertPage;
