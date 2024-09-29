import React, { useState, useEffect } from 'react';
import { Line, Pie } from 'react-chartjs-2';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend } from 'chart.js';
import { Dropdown } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../App.css';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend);

const DeliveryComponent = () => {
  const [overviewData, setOverviewData] = useState({
    totalShipments: 0,
    totalOrders: 0,
    ongoingShipments: 0,
    delivered: 0,
  });

  const [deliveries, setDeliveries] = useState([]);
  const [selectedDelivery, setSelectedDelivery] = useState(null);
  const [profitsData, setProfitsData] = useState({ weekly: 0, monthly: 0, yearly: 0 });

  useEffect(() => {
    const fetchOverviewData = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/overview'); // 
        const data = await response.json();
        setOverviewData(data);
      } catch (error) {
        console.error('Error fetching overview data:', error);
      }
    };

    const fetchDeliveries = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/deliveries'); // 
        const data = await response.json();
        setDeliveries(data);
        setSelectedDelivery(data[0]); 
      } catch (error) {
        console.error('Error fetching deliveries:', error);
      }
    };

    fetchOverviewData();
    fetchDeliveries();
  }, []);

  const fetchOrderDetails = async (orderId) => {
    try {
      const response = await fetch(`http://localhost:8080/api/orders/${orderId}`); 
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
          <a href="#"><i className="bi bi-truck me-2"></i> Shipments</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-bag me-2"></i> Orders</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-chat-dots me-2"></i> Messages</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-people me-2"></i> Customers</a>
        </li>
        <li>
          <a href="#"><i className="bi bi-person-badge me-2"></i> Employees</a>
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
              <input type="text" className="form-control me-2" placeholder="Search..." style={{ width: '100%' }} />
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
                  <Dropdown.Item href="#">View Profile</Dropdown.Item>
                  <Dropdown.Item href="#">Update Profile</Dropdown.Item>
                  <Dropdown.Item href="#">Logout</Dropdown.Item>
                </Dropdown.Menu>
              </Dropdown>
            </div>
          </header>
          </header>

          <section className="overview">
            <div className="card total-shipments">
              <h3>Total Shipments</h3>
              <p>{overviewData.totalShipments}</p>
              <span>+13.4% vs Last Week</span>
            </div>
            <div className="card total-orders">
              <h3>Total Orders</h3>
              <p>{overviewData.totalOrders}</p>
              <span>-23.4% vs Last Week</span>
            </div>
            <div className="card total-shipments">
              <h3>Total Shipments</h3>
              <p>{overviewData.ongoingShipments}</p>
              <span>+13.4% vs Last Week</span>
            </div>
            <div className="card delivered">
              <h3>Delivered</h3>
              <p>{overviewData.delivered}</p>
              <span>-13.4% vs Last Week</span>
            </div>
          </section>

          <section className="ongoing-delivery mt-4 d-flex border-top pt-3">
            <div className="delivery-list col-7">
              <h2>Ongoing Delivery</h2>
              {deliveries.map((delivery) => (
                <div
                  key={delivery.id}
                  className="delivery-item d-flex justify-content-between align-items-center border-bottom p-2"
                  onClick={() => handleDeliveryClick(delivery)}
                >
                  <div>
                    <p>Shipment number: <strong>{delivery.shipmentNumber}</strong></p>
                    <p>{delivery.route}</p>
                  </div>
                  <i className="bi bi-truck" style={{ fontSize: '2rem' }}></i>
                </div>
              ))}
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
