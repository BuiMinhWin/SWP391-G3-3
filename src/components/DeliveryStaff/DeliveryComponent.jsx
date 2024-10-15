  import React, { useState, useEffect } from 'react';
  import { Line } from 'react-chartjs-2';
  import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend, Filler  } from 'chart.js';
  import { Dropdown } from 'react-bootstrap';
  import 'bootstrap/dist/css/bootstrap.min.css';
  import './DeliveryStaff.css';
  import { useNavigate } from 'react-router-dom';
  import { listOrder,getOrderDetail } from '../../services/DeliveryService';
  import { logout } from '../Member/auth'; 
  import { FaSearch } from "react-icons/fa";

  ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend, Filler );


  const DeliveryComponent = () => {
    const handleLogout = () => {
      logout(); 
      navigate('/'); 
    };

    const handleViewOrder = (orderId) => {
      navigate(`/order/${orderId}`);
    };
    const [orders, setOrders] = useState([]);
    const navigate = useNavigate();

    const [hoveredOrder, setHoveredOrder] = useState(null); 
    const [searchQuery, setSearchQuery] = useState('');
    const [orderDetail, setOrderDetail] = useState(null);

    const [selectedDelivery, setSelectedDelivery] = useState(null);
    const [monthFilter, setMonthFilter] = useState('');
    const [provinceFilter, setProvinceFilter] = useState('');
    const [provinces, setProvinces] = useState([]);
    const [statusFilter, setStatusFilter] = useState('');
    const [transportationFilter, setTransportationFilter] = useState('');
    
    const getOrderCounts = () => {
      const totalOrders = orders.length;
      const delivering = orders.filter(order => order.status >= 1 && order.status <= 3).length;
      const approving = orders.filter(order => order.status === 0).length;
      const fail = orders.filter(order => order.status === 4).length;
    
      return {
        totalOrders,
        delivering,
        approving,
        fail,
      };
    };

    const { totalOrders, delivering, delivered, fail } = getOrderCounts();
    

    useEffect(() => {
      
      const fetchProvinces = async () => {
        try {
          const response = await fetch('https://provinces.open-api.vn/api/');
          const data = await response.json();
          console.log(data);
          setProvinces(data);
        } catch (error) {
          console.error('Error fetching provinces:', error);
        }
      };
  
      fetchProvinces();
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

    const handleSearch = async (event) => {
      const orderId = event.target.value;
      setSearchQuery(orderId);
    
      if (orderId) {
        try {
          
          const response = await getOrderDetail(orderId);
          
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
    const getStatusCounts = () => {
      const statusCounts = orders.reduce((acc, order) => {
        const status = order.status;
        if (!acc[status]) {
          acc[status] = 0;
        }
        acc[status]++;
        return acc;
      }, {});

    
      return [
        // statusCounts[0] || 0,
        statusCounts[1] || 0,
        statusCounts[2] || 0,
        statusCounts[3] || 0,
        statusCounts[4] || 0,
        // statusCounts[5] || 0,
      ];
    };
    
    const ordersByStatusChartData = {
      labels: ['Status 1', 'Status 2', 'Status 3', 'Status 4'], 
      datasets: [
        {
          label: 'Number of Orders by Status', 
          data: getStatusCounts(), 
          backgroundColor: 'rgba(75, 192, 192, 0.2)', 
          borderColor: 'rgba(75, 192, 192, 1)', 
          borderWidth: 2, 
          fill: true, 
        },
      ],
    };
    const chartOptions = {
      scales: {
        y: {
          ticks: {
            stepSize: 1,
            beginAtZero: true,
          },
          min: 0,
          max: Math.max(...getStatusCounts()) + 1, // Dynamically adjust max
        },
      },
    };

    // const handleFilterChange = () => {
    //   getAllOrders();  
    // };
    const host = "https://provinces.open-api.vn/api/";
    const filteredOrders = orders.filter(order => {
      const orderMonth = new Date(order.orderDate).getMonth() + 1;
      const matchesMonth = monthFilter ? orderMonth === parseInt(monthFilter) : true;
      const matchesProvince = provinceFilter ? order.destination.includes(provinceFilter) : true;
      const matchesStatus = statusFilter ? order.status === parseInt(statusFilter) : true;
      const matchesTransportation = transportationFilter ? order.freight === transportationFilter : true;
    
      return matchesMonth && matchesProvince && matchesStatus && matchesTransportation;
    });

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
            <a href="#"><i className="bi bi-chat-dots me-2"></i> Messages</a>
          </li>
          <li>
            <a href="/orders"><i className="bi bi-bag me-2"></i> Orders</a>
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
              <span className="search-icon">
                <FaSearch />
             </span>
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
                    <Dropdown.Item href="user-page">View Profile</Dropdown.Item>
                    <Dropdown.Item href="#">Update Profile</Dropdown.Item>
                    <Dropdown.Item onClick={handleLogout}>Logout</Dropdown.Item>
                  </Dropdown.Menu>
                </Dropdown>
              </div>
            </header>
            </header>

            <section className="overview">
              <div className="card total-orders">
                <h3>Total Orders</h3>
                <p>{totalOrders}</p>
              </div>

              <div className="card delivering">
                <h3>Delivering</h3>
                <p>{delivering}</p>
              </div>

              <div className="card approving">
                <h3>Approving</h3>
                <p>{delivered}</p>
              </div>
                
              <div className="card fail">
                <h3>Fail</h3>
                <p>{fail}</p>
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
            <div className="delivery-list col-12">
                <h2>List of Orders</h2>

                <div className="filter-bar d-flex mb-3">
                  <select className="form-select me-2" value={monthFilter} onChange={(e) => setMonthFilter(e.target.value)}>
                    <option value="">All Months</option>
                    <option value="1">January</option>
                    <option value="2">February</option>
                    <option value="3">March</option>
                    <option value="4">April</option>
                    <option value="5">May</option>
                    <option value="6">June</option>
                    <option value="7">July</option>
                    <option value="8">August</option>
                    <option value="9">September</option>
                    <option value="10">October</option>
                    <option value="11">November</option>
                    <option value="12">December</option>
                  </select>
                  {/* <select className="form-select me-2" value={regionFilter} onChange={(e) => setRegionFilter(e.target.value)}>
                    <option value="">All Regions</option>
                    <option value="North">North</option>
                    <option value="South">South</option>
                    {/* Add other regions */}
                  {/* </select>  */}

                  <select className="form-select me-2" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
                    <option value="">All Statuses</option>
                    <option value="0">Waiting for approval</option>
                    <option value="1">In Progress</option>
                    <option value="2">Delivering</option>
                    <option value="3">Delivered</option>
                    {/* Add other statuses */}
                  </select>
                  <select className="form-select me-2" value={transportationFilter} onChange={(e) => setTransportationFilter(e.target.value)}>
                    <option value="">All Transport</option>
                    <option value="Truck">Truck</option>
                    <option value="Van">Van</option>
                    <option value="car">Car</option>
                    {/* Add other transportation methods */}
                  </select>

                  
                  <select
                    className="form-select me-2"
                    value={provinceFilter}
                    onChange={(e) => setProvinceFilter(e.target.value)}
                  >
                    <option value="">Chọn tỉnh thành</option>
                    {provinces.map((province) => (
                      <option key={province.code} value={province.name}>
                        {province.name}
                    </option>
                    ))}
                  </select>
            
                </div>
                
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
                    <th></th>
                    </tr>
                  </thead>
                  <tbody>
                  {filteredOrders.length > 0 ? (
                    filteredOrders.map((order) => (
                      <tr
                        key={order.orderId}
                        onMouseEnter={() => handleMouseEnter(order)}
                        onMouseLeave={handleMouseLeave}
                      >
                        <td>{order.orderId}</td>
                        <td>{order.destination}</td>
                        <td>{order.freight}</td>
                        <td>{order.orderDate}</td>
                        <td>{order.shippedDate}</td>
                        <td>{order.totalPrice}</td>
                        <td>{order.origin}</td>
                        <td>{order.status}</td>
                        <td>
                          <button onClick={() => handleViewOrder(order.orderId)}>View</button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="12" className="text-center">No Orders Found</td>
                    </tr>
                  )}
                </tbody>
                </table>

              </div>
            
            </section>

            <section className="statistics mt-4 d-flex justify-content-between border-top pt-3">
              
              <div className="container">
                <h2>Orders by Status</h2>
                <Line data={ordersByStatusChartData} options={chartOptions} />
              </div>
            </section>
          </main>
        </div>
      </div>
    );
  };

  export default DeliveryComponent;
