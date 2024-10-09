import React, { useState, useEffect } from 'react';
import './SaleStaff.css';
import { useNavigate } from 'react-router-dom';
import { listOrders, updateOrderStatus } from '../../services/SaleStaffService'; // Import the service

const SalesStaff = () => {
  const [orders, setOrders] = useState([]); // State to manage orders list
  const navigate = useNavigate();

  // Fetch orders when component mounts
  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    try {
      const response = await listOrders();  // Call the listOrders function from service
      setOrders(response.data);
    } catch (error) {
      console.error('Error fetching orders:', error);
    }
  };

  const handleUpdateStatus = async (orderId, newStatus) => {
    try {
      // Call the service function to update the order status
      await updateOrderStatus(orderId, newStatus);
      // Refresh orders list after the status is updated
      fetchOrders();
    } catch (error) {
      console.error('Error updating order status:', error);
    }
  };

  return (
    <div className="sales-staff-container">
      <h1>Quản lý Đơn hàng</h1>

      <table className="order-table">
        <thead>
          <tr>
            <th>Mã Đơn Hàng</th>
            <th>Khách Hàng</th>
            <th>Trạng Thái</th>
            <th>Hành Động</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order) => (
            <tr key={order.orderId}>
              <td>{order.orderId}</td>
              <td>{order.accountId}</td>
              <td>{order.status}</td>
              <td>
                {order.status === 'Pending' && (
                  <button onClick={() => handleUpdateStatus(order.orderId, 'In Delivery')}>Bắt đầu vận chuyển</button>
                )}
                {order.status === 'In Delivery' && (
                  <button onClick={() => handleUpdateStatus(order.orderId, 'Delivered')}>Đã giao</button>
                )}
                {order.status === 'Delivered' && <span>Đã hoàn tất</span>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <button className="back-btn" onClick={() => navigate('/')}>Quay lại Trang chủ</button>
    </div>
  );
};

export default SalesStaff;
