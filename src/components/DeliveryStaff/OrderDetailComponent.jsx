import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { getOrderDetail } from '../../services/DeliveryService';

const OrderDetailComponent = () => {
  const {orderId,setOrderId } = useState([]);
  // const [orderDetail, setOrderDetail] = useState(null);

  useEffect(() => {
    if (orderId) {
      getOrderDetail(orderId)
        .then(response => {
          console.log(response.data); // Log the response to ensure it matches
          setOrderId([response.data]); // Wrap in array if expecting multiple records
        })
        .catch(error => console.error('Error fetching order details:', error));
    } else {
      console.log("Not found");
    }
  }, [orderId]);

  return (
    <div className="order-detail">
  {orderId ? (
    <div>
      <h2>Order Details for Order ID: {orderId}</h2>
      {orderId.map((order) => (
        <div key={order.orderId} className="order-detail-item">
          <p>CreatedAt: {order.createdAt}</p>
          <p>KoiName: {order.koiName}</p>
          <p>KoiType: {order.koiType}</p>
          <p>Quantity: {order.quantity}</p>
          <p>Weight: {order.weight}</p>
          <p>Status: {order.status}</p>
        </div>
      ))}
    </div>
  ) : (
    <p>Loading order details...</p>
  )}
</div>
  );
};

export default OrderDetailComponent;
