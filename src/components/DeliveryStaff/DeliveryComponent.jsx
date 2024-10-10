import React, { useState, useEffect } from 'react';
import { Line, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import { Dropdown } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import './DeliveryStaff.css';
import { useNavigate } from 'react-router-dom';
import { listOrder,getOrderDetail } from '../../services/DeliveryService';


ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend);

const DeliveryComponent = () => {
  const [overviewData, setOverviewData] = useState({
    totalShipments: 0,
    totalOrders: 0,
    ongoingShipments: 0,
    delivered: 0,
  });
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  const [selectedDelivery, setSelectedDelivery] = useState(null);
  const [profitsData, setProfitsData] = useState({ weekly: 0, monthly: 0, yearly: 0 });
  const [hoveredOrder, setHoveredOrder] = useState(null); 
  const [searchQuery, setSearchQuery] = useState('');
  const [orderDetail, setOrderDetail] = useState(null);

  useEffect(() => {
  // const token = localStorage.getItem('userToken');
  const roleId = localStorage.getItem('userRole'); 
  // console.log('Token:', token);
  console.log('Role ID:', roleId);
  const accountId = localStorage.getItem('accountId');
console.log("Stored Account ID:", accountId);
    const fetchOverviewData = async () => {
      try {
        const response = await fetch('http://koideliverysystem.id.vn:8080/api/orders'); 
        const data = await response.json();
        setOverviewData(data);
      } catch (error) {
        console.error('Error fetching overview data:', error);
      }
    };

    const fetchDeliveries = async () => {
      try {
        const response = await fetch('http://koideliverysystem.id.vn:8080/api/orders'); 
        const data = await response.json();
        
        setSelectedDelivery(data[0]); 
      } catch (error) {
        console.error('Error fetching deliveries:', error);
      }
    };

    fetchOverviewData();
    fetchDeliveries();
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
        console.error("Error fetching : ", error);
      });
  };
  
  const handleMouseEnter = (order) => {
    setHoveredOrder(order); 
  };

  const handleMouseLeave = () => {
    setHoveredOrder(null); 
  };
  const fetchOrderDetails = async (orderId) => {
    try {
      const response = await fetch('http://localhost:8080/api/orders/${orderId}'); 
      const data = await response.json();
    
      updateChartData(data);
    } catch (error) {
      console.error('Error fetching order details:', error);
    }
  };

  const updateChartData = (orderData) => {
  
    setProfitsData({
      weekly: orderData.weeklyProfit,
      monthly: orderData.monthlyProfit,
      yearly: orderData.yearlyProfit,
    });
  };

  const handleDeliveryClick = (delivery) => {
    setSelectedDelivery(delivery);
    fetchOrderDetails(delivery.orderId); 
  };

  const shipmentsChartData = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug'],
    datasets: [
      {
        label: 'Shipments',
        data: [20, 10, 5, 2, 20, 30, 45, 50], 
        backgroundColor: 'rgba(75, 192, 192, 0.2)',
        borderColor: 'rgba(75, 192, 192, 1)',
        borderWidth: 1,
      },
    ],
  };

  const profitsChartData = {
    labels: ['Weekly', 'Monthly', 'Yearly'],
    datasets: [
      {
        data: [profitsData.weekly, profitsData.monthly, profitsData.yearly], 
        backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
        hoverBackgroundColor: ['#FF6384', '#36A2EB', '#FFCE56'],
      },
    ],
  };

  const handleSearch = async (event) => {
    const query = event.target.value;
    setSearchQuery(query);
  
    if (query) {
      try {
        
        const response = await getOrderDetail(query);
        
        if (response.data) {
          setOrderDetail(response.data);  
        } else {
          setOrderDetail(null);  
        }
      } catch (error) {
        console.error("Error fetching order details:", error);
        setOrderDetail(null);  
      }
    } else {
      setOrderDetail(null);  
    }
  };
  

  return (
    <div className="container-fluid">
      <div className="row">
        <aside className="sidebar col-2 p-3 border-end">
          <div className="profile-container text-center mb-4">
            <div className="SideKoi d-flex align-items-center justify-content-between">
              <img src="/Logo-Koi/Order.png" alt="Profile " className="profile-img rounded-circle me-3" />
              <div className="text-start KoiLogo">
                <p className="KoiDeli mb-0">Koi Deli</p>
              </div>
            </div>
            <hr className="logo-separator" />
            
          </div>
          <nav>
      <ul className="list-unstyled">
        <li>
          <a href="#"><i className="bi bi-speedometer2 me-2"></i> Dashboard</a>
        </li>
        <li>
          <a href="/orders"><i className="bi bi-bag me-2"></i> Orders</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-chat-dots me-2"></i> Messages</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-life-preserver me-2"></i> Help & Support</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-gear me-2"></i> Settings</a>
        </li>
      </ul>
    </nav>

        </aside>

        <main className="dashboard col-10 p-4">
        <header className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
            <h1>Dashboard</h1>
            <header className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
            <div className="d-flex align-items-center search-container" style={{ flex: 1 }}>
            <input
                type="text"
                className="form-control me-2"
                placeholder="Search by Order ID..."
                value={searchQuery}
                onChange={handleSearch}
                style={{ width:    '100%' }}
              />
            </div>
            <div className="d-flex align-items-center">
              <select className="form-select me-2">
                <option>ENG</option>
                <option>FR</option>
                <option>ES</option>
              </select>
              <Dropdown>
                <Dropdown.Toggle variant="secondary" id="dropdown-basic" className="profile-dropdown">
                  <img src="/Delivery/User.png" alt="Profile" className="profile-img rounded-circle" style={{ width: '40px', height: '40px' }} />
                </Dropdown.Toggle>
                <Dropdown.Menu>
                  <Dropdown.Item href="/profile">View Profile</Dropdown.Item>
                  <Dropdown.Item href="#">Update Profile</Dropdown.Item>
                  <Dropdown.Item href="#">Logout</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </div>
          </header>
          </header>

          <section className="overview">
            <div className="card total-shipments">
              <h3>Total Orders</h3>
              <p>{overviewData.totalShipments}</p>
              
            </div>
            <div className="card total-orders">
              <h3>Delivering</h3>
              <p>{overviewData.totalOrders}</p>
              
            </div>
            <div className="card total-shipments">
              <h3>Delivered</h3>
              <p>{overviewData.ongoingShipments}</p>
              
            </div>
            <div className="card delivered">
              <h3>Fail</h3>
              <p>{overviewData.delivered}</p>
              
            </div>
          </section>

          {orderDetail && orderDetail.length > 0 && (
          <section className="filtered-order mt-4">
            <h2>Order Details for ID: {orderDetail[0].orderId}</h2>
            
            <table className="table table-bordered">
              <thead>
                <tr>
                  <th>Order Detail ID</th>
                  <th>Koi Name</th>
                  <th>Koi Type</th>
                  <th>Quantity</th>
                  <th>Weight</th>
                  <th>Discount</th>
                  <th>Status</th>
                  <th>Created At</th>
                  
                </tr>
              </thead>
              <tbody>
                {orderDetail.map((detail) => (
                  <tr key={detail.orderDetailId}>
                    <td>{detail.orderDetailId}</td>
                    <td>{detail.koiName}</td>
                    <td>{detail.koiType}</td>
                    <td>{detail.quantity}</td>
                    <td>{detail.weight}</td>
                    <td>{detail.discount}</td>
                    <td>{detail.status === 1 ? 'Active' : 'Inactive'}</td>
                    <td>{new Date(detail.createdAt).toLocaleString()}</td>
                    
                  </tr>
                ))}
              </tbody>
            </table>
          </section>
)}

          <section className="ongoing-delivery mt-4 d-flex border-top pt-3">
          <div className="delivery-list col-7">
              <h2>Ongoing Delivery</h2>
              <table className="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th>OrderId</th>
                    <th>TotalPrice</th>
                    <th>Status</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.length > 0 ? (
                    orders.map((order) => (
                      <tr
                        key={order.orderId}
                        onMouseEnter={() => handleMouseEnter(order)} 
                        onMouseLeave={handleMouseLeave} 
                      >
                        <td>{order.orderId}</td>
                        <td>{order.totalPrice}</td>
                        <td>{order.status}</td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="3" className="text-center">No Orders Found</td>
                    </tr>
                  )}
                </tbody>
              </table>

              {hoveredOrder && (
                <div className="hovered-order-details mt-3 p-3 bg-light border rounded">
                <h4>Order Details</h4>
                <p><strong>Destination:</strong> {hoveredOrder.destination}</p>
                <p><strong>Freight:</strong> {hoveredOrder.freight}</p>
                <p><strong>OrderDate:</strong> {hoveredOrder.orderDate}</p>
                <p><strong>ShipDate:</strong> {hoveredOrder.shippedDate}</p>
                <p><strong>Origin:</strong> {hoveredOrder.origin}</p>
              </div>
              )}
            </div>
            <div className="delivery-map col-5">
              <img src="/Delivery/map.png" alt="Map" className="img-fluid" />
              {selectedDelivery && (
                <div className="delivery-details d-flex justify-content-between mt-2">
                  <p><strong>Category:</strong> {selectedDelivery.category}</p>
                  <p><strong>Distance:</strong> {selectedDelivery.distance}</p>
                  <p><strong>Estimation:</strong> {selectedDelivery.estimation}</p>
                  <p><strong>Weight:</strong> {selectedDelivery.weight}</p>
                  <p><strong>Fee:</strong> {selectedDelivery.fee}</p>
                </div>
              )}
            </div>
          </section>

          <section className="statistics mt-4 d-flex justify-content-between border-top pt-3">
            <div className="chart">
              <h3>Shipments & Orders</h3>
              <Line data={shipmentsChartData} />
            </div>
            <div className="profits bg-light rounded p-3 shadow">
              <h3>Profits</h3>
              <Pie data={profitsChartData} />
            </div>
          </section>
        </main>
      </div>
    </div>
  );
};

export default DeliveryComponent;