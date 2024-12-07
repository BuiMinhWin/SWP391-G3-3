import React, { useState, useEffect } from 'react';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend, Filler  } from 'chart.js';
import 'bootstrap/dist/css/bootstrap.min.css';
import './DeliveryStaff.css';
import { useNavigate } from 'react-router-dom';
import { listOrder, getOrderDetail, updateStatus } from '../../services/DeliveryService';
import { logout } from '../Member/auth'; 
import { FaRegCalendarAlt } from "react-icons/fa";
import { FiHome } from "react-icons/fi";
import { IoSettingsOutline } from "react-icons/io5";
import { MdSupportAgent} from "react-icons/md";
import { IoIosNotificationsOutline } from "react-icons/io";
import { HiOutlineClipboardDocumentList } from "react-icons/hi2";
import { FaRegMessage } from "react-icons/fa6";
import { CgProfile } from "react-icons/cg";
import { CiLogout } from "react-icons/ci";
import { FaTruckFast } from "react-icons/fa6";
import { FiAlertTriangle } from "react-icons/fi";
import { FaRegRectangleList } from "react-icons/fa6";
import { FaBoxesStacked } from "react-icons/fa6";
import {  getAvatar} from "../../services/CustomerService";
import { trackingOrderState } from '../../services/DeliveryStatusService';
import { useSnackbar } from 'notistack';
import axios from "axios";

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, ArcElement, Title, Tooltip, Legend, Filler );


const DeliveryComponent = () => {
  const { enqueueSnackbar, closeSnackbar } = useSnackbar();

  const handleLogout = () => {
    logout(); 
    navigate('/'); 
  };

  
const toggleDropdown = () => {
  setDropdownOpen(!isDropdownOpen);
}

  const handleViewOrder = (orderId) => {
    navigate(`/order/${orderId}`);
  };
  const [orders, setOrders] = useState([]);
  const navigate = useNavigate();

  const [searchQuery, setSearchQuery] = useState('');
  const [avatar, setAvatar] = useState(null); 

  const [monthFilter, setMonthFilter] = useState('');
  const [provinceFilter, setProvinceFilter] = useState('');
  const [provinces, setProvinces] = useState([]);
  const [statusFilter, setStatusFilter] = useState('');
  const [transportationFilter, setTransportationFilter] = useState('');


  const [isDropdownOpen, setDropdownOpen] = useState(true); 

  const accountId = localStorage.getItem("accountId");
      console.log("accountId:", accountId);


  
  const GHN_API_KEY=import.meta.env.VITE_GHN_API_KEY;
  useEffect(() => {
    
    const fetchProvinces = async () => {
      try {
        const response = await fetch('https://online-gateway.ghn.vn/shiip/public-api/master-data/province', {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            'Token': GHN_API_KEY,
          },
        });
        const data = await response.json();
        // console.log(data); 
        setProvinces(data.data); 
      } catch (error) {
        console.error('Error fetching provinces:', error);
      }
    };
    const fetchAccount = async () => {
      try {
       
        const avatarUrl = await getAvatar(accountId);
        setAvatar(avatarUrl);
      } catch (error) {
        console.error("Error fetching account data:", error);
      } 
    };
 
    if (accountId) fetchAccount();

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

  const handleSearch = async (event) => {
    setSearchQuery(event.target.value.toLowerCase());
  };

  const API_KEY =import.meta.env.VITE_GOONG_API_KEY; // Thay bằng API Key của bạn

  const reverseGeocodeAddress = async (lat, long) => {
    try {
      const response = await axios.get(
        `https://rsapi.goong.io/Geocode?latlng=${lat},${long}&api_key=${API_KEY}`
      );
      const data = response.data;
      if (data.results && data.results.length > 0) {
        const address = data.results[0].formatted_address; // Get the formatted address
        return address; // Return the full address
      } else {
        throw new Error('No results found for the address.');
      }
    } catch (error) {
      console.error('Error fetching geocode:', error);
      throw new Error('Failed to fetch geocode.');
    }
  };
 
  const filteredOrders = orders.filter(order => {
    const orderMonth = new Date(order.orderDate).getMonth() + 1;
    const matchesMonth = monthFilter ? orderMonth === parseInt(monthFilter) : true;
    const matchesProvince = provinceFilter ? order.destination.includes(provinceFilter) : true;
    const matchesStatus = statusFilter ? order.status === parseInt(statusFilter) : true;
    const matchesTransportation = transportationFilter ? order.orderNote === transportationFilter : true;

    return matchesMonth && matchesProvince && matchesStatus && matchesTransportation;
  })
  .filter(order => 
    (order.orderId && order.orderId.toString().toLowerCase().includes(searchQuery)) ||
    (order.customerName && order.customerName.toLowerCase().includes(searchQuery)) ||
    (order.destination && order.destination.toLowerCase().includes(searchQuery))
  );


  return (
    <div className="container-fluid">
      <div className="row">
        <aside className="sidebar col-2 p-3 ">
          <div className='manager-sidebar'>
          <div className="profile-container text-center mb-4">
            <div className="SideKoi d-flex align-items-center justify-content-between">
              <img src="/Logo-Koi/Order.png" alt="Profile "className="profile-img rounded-circle " />
              <div className=" KoiLogo">
                <p className="KoiDeli ">Koi Deli</p>
              </div>
            </div>
            {/* <hr className="logo-separator" />  */}
            {/* border */}
            
          </div>
          <nav>
      <ul className="list-unstyled">

        {/* <li>
          <a href="#"><i className="bi bi-speedometer2 me-2"></i> Dashboard</a>
          
        </li> */}
        
          <li>
            <a href="/"><i className="bi bi-speedometer2 me-2"> <FiHome /> </i>  Homepage</a>
        </li>

        

        <li>
          <a href="/delivery"><i className="bi bi-person-badge me-2"><HiOutlineClipboardDocumentList /></i> Ordering</a>
        </li>

        <li>
          <a href="#"><i className="bi bi-chat-dots me-2"><FaRegCalendarAlt /></i> Calendar</a>
         </li>

        <li>
          <a href="#"><i className="bi bi-life-preserver me-2"><MdSupportAgent /></i> Help & Support</a>
        </li>

        <li>
          <a href="#"><i className="bi bi-chat-dots me-2"> <FaRegMessage/> </i>  Messages</a>
        </li>

        <li>
          <a href="#"><i className="bi bi-gear me-2"><IoSettingsOutline /></i> Settings</a>
         </li>
       
      </ul>
      </nav>
      </div>
        </aside>

        <main className="dashboard col-10 ">
        <header className="d-flex justify-content-between align-items-center mb-4 ">
            <h4 className="title">Delivery History</h4> 
            {/* <h6>Delivery Orders</h6>          */}
            <header className="d-flex justify-content-between align-items-center mb-4 ">
            <div className="header-content" style={{ width: '%' }}> 
            <div className="d-flex align-items-center justify-content-center search-container">
            <input
              className="search-bar"
              type="text"
              value={searchQuery}
              onChange={handleSearch}
              placeholder="Search Order"
            />
           </div>

              <div className="navbar-cus-right">
                <div className="dropdown" onClick={toggleDropdown}>
                <img src={avatar || '/default-avatar.png'} alt="Avatar" className="avatar" />
                  {isDropdownOpen && ( 
                    <div className="dropdown-content">
                      <a  href="user-page"><CgProfile /> View Profile</a>
                      <a  onClick={handleLogout}><CiLogout /> Logout</a>
                    </div>
                  )}
                </div>
              </div>
            

            </div>

            <div className="notification-icon m-4">
                <IoIosNotificationsOutline />
               
              </div>
          </header>

          </header>

          <section className="delivery-ongoing-delivery mt-4 d-flex border-top pt-3">
          <div className="delivery-list col-12 " >

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
              
                <select className="form-select me-2" value={statusFilter} onChange={(e) => setStatusFilter(e.target.value)}>
                  <option value="">All Statuses</option>
                  <option value="3">Waiting for get order</option>
                  <option value="4">Deliverin</option>
                  <option value="5">Delivered</option>
                  
                 
                </select>
                <select className="form-select me-2" value={transportationFilter} onChange={(e) => setTransportationFilter(e.target.value)}>
             
                  <option value= "">Method Transport</option>
                  <option value= "Giao hàng khẩn cấp">Express Delivery</option>
                  <option value= "Giao hàng tiêu chuẩn">Regular Delivery</option>
                </select>

                
                <select className="form-select me-2" value={provinceFilter} onChange={(e) => setProvinceFilter(e.target.value)}>
                <option value="">All Provinces</option>
                {provinces?.map((province) => (
                  <option key={province.ProvinceID} value={province.ProvinceName}>
                    {province.ProvinceName}
                  </option>
                ))}
              </select>
              </div>
              
              <table className="table table-striped table-bordered ">
                <thead>
                  <tr>
                  <th>OrderId</th>
                  <th>Origin</th>
                  <th>OrderDate</th>
                  <th>Destination</th>
                  <th>ShippedDate</th>
                  <th>Freight</th>
                  <th>Status</th>
                  <th>Details</th>
                  </tr>
                </thead>
                <tbody>
                {filteredOrders.length > 0 ? (
                  filteredOrders
                  .filter(order => order.deliver ===accountId&& order.status > 1 ) 
                  .sort((a, b) => new Date(b.orderDate) - new Date(a.orderDate))
                  .map((order) => (
                    <tr key={order.orderId}>
                      <td>{order.orderId}</td>
                      <td>{order.origin}</td>
                      <td>{new Date(order.orderDate).toLocaleDateString()}</td>
                      <td>{order.destination}</td>
                      <td>
                        {order.shippedDate ? new Date(order.shippedDate).toLocaleDateString() : 'Chưa giao hàng'}
                      </td>
                      <td>{order.freight}</td>
                      <td>
                        {order.status === 2 && "Đang lấy hàng"}
                        {order.status === 3 && "Đã lấy hàng"}
                        {order.status === 4 && "Đang giao"}
                        {order.status === 5 && "Đã hoàn thành"}  
                      </td>
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

        </main>
      </div>
    </div>
  );
};

export default DeliveryComponent;
