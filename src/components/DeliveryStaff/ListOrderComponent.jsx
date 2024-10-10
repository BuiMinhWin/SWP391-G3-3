import React, { useEffect, useState } from 'react';
import { listOrder, updateStatus } from '../../services/DeliveryService';
import { useNavigate } from 'react-router-dom';

const ListOrderComponent = () => {
  const [orders, setOrders] = useState([]);
  const [editedStatuses, setEditedStatuses] = useState({});
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
    // console.log("Before update:", editedStatuses);
  
    const updatedStatuses = {
      ...editedStatuses,  
      [orderId]: newStatus,  
    };
  
    // console.log("After update:", updatedStatuses);
  
    setEditedStatuses(updatedStatuses); 
  };

  const updateOrderStatus = (orderId) => {
    const newStatus = editedStatuses[orderId] ?? order.status;
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

  return (
    <div className="container">
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
