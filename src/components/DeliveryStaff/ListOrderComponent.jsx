import React, { useEffect, useState } from 'react';
import { listOrder, updateStatus, getOrderDetail,getOrderByProvince } from '../../services/DeliveryService';
import { FaLongArrowAltLeft } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';

const ListOrderComponent = () => {
  const [orders, setOrders] = useState([]);
  const [editedStatuses, setEditedStatuses] = useState({});
  // const [orderDetail, setOrderDetail] = useState(null); // Lưu chi tiết đơn hàng ở đây
  const navigate = useNavigate();
  const province = localStorage.getItem('province'); 
  console.log(province);

  useEffect(() => {
    getAllOrders();
    if (province) {
      getAllOrdersByProvince(province); // Gọi API lấy đơn hàng theo tỉnh
    }
  }, [province]);

  const getAllOrders = () => {
    listOrder()
      .then((response) => {
        if (Array.isArray(response.data)) {
          setOrders(response.data);
        } else {
          console.error("API response is not an array", response.data);
          setOrders([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching orders: ", error);
      });
  };

  const handleStatusChange = (orderId, newStatus) => {
    const updatedStatuses = {
      ...editedStatuses,
      [orderId]: newStatus,
    };
    setEditedStatuses(updatedStatuses);
  };

  const updateOrderStatus = (orderId) => {
    const newStatus = editedStatuses[orderId] ?? orders.find(order => order.orderId === orderId)?.status;
    if (newStatus) {
      console.log('New status to update:', newStatus);
      updateStatus(orderId, newStatus)
        .then((response) => {
          console.log('Status updated successfully:', response);
          getAllOrders();
        })
        .catch((error) => {
          console.error('Error updating status:', error);
        });
    }
  };

  const handleViewOrder = (orderId) => {
    navigate(`/order/${orderId}`);
  };

  const roleId = localStorage.getItem('roleId'); 
  

  const handleBackClick = () => {
    if (roleId === 'Manager') {
      navigate('/manager'); // Điều hướng cho manager
    }  else if (roleId === 'Delivery') {
      navigate('/delivery'); // Điều hướng cho delivery
    } else {
      navigate('/'); // Điều hướng về homepage
    }
  };

  const getAllOrdersByProvince = (province) => {
    getOrderByProvince(province)
      .then((response) => {
        if (Array.isArray(response.data)) {
          setOrders(response.data);
        } else {
          console.error("API response is not an array", response.data);
          setOrders([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching orders by province: ", error);
      });
  };

  return (
    <div className="container">
       <button
        type="button"
        onClick={handleBackClick}
        style={{
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          position: 'absolute',
          top: '10px',
          left: '120px',
          padding: '5px',
          fontSize: '14px',
          display: 'flex',
          alignItems: 'center',
        }}
      >
        <FaLongArrowAltLeft size={16} color="black" />
        <span style={{ marginLeft: '15px' }}>Back</span>
      </button>

      <h2 className="text-center">List of Orders</h2>
      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>OrderId</th>
            <th>Destination</th>
            <th>Freight</th>
            <th>OrderDate</th>
            <th>ShipDate</th>
            <th>TotalPrice</th>
            <th>Origin</th>
            <th>Status</th>
            <th>Actions</th>
            <th>Details</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders
            .filter(order => order.province === province) // Lọc các đơn hàng có tỉnh trùng với localStorage
            .map(order => (
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.destination}</td>
                <td>{order.freight}</td>
                <td>{order.orderDate}</td>
                <td>{order.shippedDate}</td>
                <td>{order.totalPrice}</td>
                <td>{order.origin}</td>
                <td>
                  <input
                    type="text"
                    value={editedStatuses[order.orderId] ?? order.status}
                    onChange={(e) => handleStatusChange(order.orderId, e.target.value)}
                  />
                </td>
                <td>
                  <button
                    className="btn btn-info"
                    onClick={() => updateOrderStatus(order.orderId)}
                  >
                    Update Status
                  </button>
                </td>
                <td>
                  <button
                    className="btn btn-primary"
                    onClick={() => handleViewOrder(order.orderId)}
                  >
                    View
                  </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="9" className="text-center">No Orders Found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ListOrderComponent;