import React, { useEffect, useState } from 'react';
import { listOrder, updateStatus } from '../../services/DeliveryService';
import { FaLongArrowAltLeft } from "react-icons/fa";
import { useNavigate } from 'react-router-dom';

const ListOrderComponent = () => {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    getAllOrders();
  }, []);

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

  const handleUpdateStatus = (orderId, currentStatus) => {
    // Only allow update if the current status is 0
    if (currentStatus === 0) {
      const newStatus = 1; // Update to 'Đơn đã được duyệt'
      updateStatus(orderId, newStatus)
        .then(() => {
          getAllOrders(); // Refresh order list after update
        })
        .catch((error) => {
          console.error("Error updating order status: ", error);
        });
    }
  };

  const handleViewOrder = (orderId) => {
    navigate(`/confirmDetail/${orderId}`);
  };

  return (
    <div className="container">
      <button
        type="button"
        onClick={() => navigate('/delivery')}
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
            <th>Update Status</th>
            <th>View</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders.map(order => (
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.destination}</td>
                <td>{order.freight}</td>
                <td>{order.orderDate}</td>
                <td>{order.shippedDate}</td>
                <td>{order.totalPrice}</td>
                <td>{order.origin}</td>
                <td>
                  {order.status === 0 ? "Đang chờ xét duyệt" : "Đơn đã được duyệt"}
                </td>
                <td>
                  {order.status === 0 ? (
                    <button
                      className="btn btn-success"
                      onClick={() => handleUpdateStatus(order.orderId, order.status)}
                    >
                      Update
                    </button>
                  ) : (
                    <button className="btn btn-secondary" disabled>
                      Already Approved
                    </button>
                  )}
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
              <td colSpan="10" className="text-center">No Orders Found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default ListOrderComponent;
