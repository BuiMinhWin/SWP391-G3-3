import React, {useEffect, useState} from 'react'
import { listOrder } from '../../services/DeliveryService';
import { useNavigate } from 'react-router-dom';

const ListOrderComponent = () => {
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate()  ;

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
        console.error("Error fetching employees: ", error);
      });
  };
  const updateOrder = (orderId) => {
    navigate(`/edit-order/${orderId}`);
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
                <td>{order.status}</td>
                <td>
                  <button className="btn btn-info" onClick={() => updateOrder(order.orderId)}>Update</button>
                  
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="text-center">No Orders Found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ListOrderComponent