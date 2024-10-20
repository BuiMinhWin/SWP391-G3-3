import React, { useEffect, useState } from 'react'; 
import { listOrder, updateStatus } from '../../services/SaleStaffService';
import { getOrderDetail } from '../../services/DeliveryService';
import { useParams } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';

const OrderDetailDocumentComponent = () => {
  const { orderId } = useParams();
  const [orders, setOrders] = useState([]);
  const [orderDetail, setOrderDetail] = useState([]);
  const [editedStatuses, setEditedStatuses] = useState({});

  useEffect(() => {
    if (orderId) {
      getOrderDetail(orderId)
        .then(response => {
          console.log(response.data);
          setOrderDetail(response.data);
        })
        .catch(error => console.error('Error fetching order details:', error));
    } else {
      console.log("Order ID not found");
    }
  }, [orderId]);

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

  const handleStatusChange = (orderId, newStatus) => {
    const updatedStatuses = {
      ...editedStatuses,
      [orderId]: newStatus,
    };
    setEditedStatuses(updatedStatuses);
  };

  const updateOrderStatus = (orderId) => {
    const newStatus = editedStatuses[orderId] ?? orders.status;
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

  const currentOrder = orders.find(order => order.orderId === orderId);

  return (
    <div className="order-detail">
      <h2>Order Details for Order ID: {orderId}</h2>

      {/* Hiển thị thông tin kết hợp từ orderDetail và orders */}
      {orderDetail.length > 0 && currentOrder ? (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>CreatedAt</th>
              <th>KoiName</th>
              <th>KoiType</th>
              <th>Quantity</th>
              <th>Weight</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {orderDetail.map((detail) => (
              <tr key={detail.orderDetailId}>
                <td>{detail.createdAt || "N/A"}</td>
                <td>{detail.koiName || "N/A"}</td>
                <td>{detail.koiType || "N/A"}</td>
                <td>{detail.quantity || "N/A"}</td>
                <td>{detail.weight || "N/A"}</td>
                <td>
                  <input
                    type="text"
                    value={editedStatuses[currentOrder.orderId] ?? currentOrder.status}
                    onChange={(e) => handleStatusChange(currentOrder.orderId, e.target.value)}
                  />
                </td>
                <td>
                  <button
                    className="btn btn-info"
                    onClick={() => updateOrderStatus(currentOrder.orderId)}
                  >
                    Update Status
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No order details available</p>
      )}
    </div>
  );
};

export default OrderDetailDocumentComponent;
