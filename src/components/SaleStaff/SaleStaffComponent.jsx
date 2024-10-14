import React, {useEffect, useState} from 'react'
import { listOrder, trackingOrder } from '../../services/SaleStaffService';
import { useNavigate } from 'react-router-dom';

const SaleStaffComponent = () => {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate()  ;

  useEffect(() => {
    getAllOrders();
  }, []);

  const getAllOrders = () => {
    listOrder()
      .then((response) => { 
        console.log(response.data);
        if (Array.isArray(response.data)) {
          setOrders(response.data);
        
        } else {
          console.error("API response is not an array", response.data);
          setOrders([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching employees: ", error);
      });
  };
   // Function to handle status update
   const handleUpdateStatus = (orderId, currentStatus) => {
  const newStatus = currentStatus === 3 ? 1 : currentStatus + 1;  

  trackingOrder(orderId, newStatus)  
    .then((response) => {
      console.log("Order status updated:", response.data);
      setOrders((prevOrders) =>
        prevOrders.map((order) =>
          order.orderId === orderId ? { ...order, status: newStatus } : order
        )
      );
    })
    .catch((error) => {
      console.error("Error updating order status:", error);
    });
};


  return (
    <div className="container">
      <h1>Sales Staff - Order Management</h1>

      <table className="table table-striped table-bordered">
        <thead>
          <tr>
            <th>Order ID</th>
            <th>Receiver Name</th>
            <th>Sender Name</th>
            <th>Origin</th>
            <th>Destination</th>
            <th>Total Price</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {orders.length > 0 ? (
            orders.map(order => (
              <tr key={order.orderId}>
                <td>{order.orderId}</td>
                <td>{order.receiverName}</td>
                <td>{order.senderName}</td>
                <td>{order.origin}</td>
                <td>{order.destination}</td>
                <td>{order.totalPrice}</td>
                <td>{order.status}</td>
                <td>
                <button onClick={() => handleUpdateStatus(order.orderId, order.status)}>
                  Update Status
                </button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="text-center no-orders">No Orders Found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default SaleStaffComponent