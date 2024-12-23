import React, { useState, useEffect } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';
import { logout } from '../Member/auth'; 
import { FaRegCalendarAlt } from "react-icons/fa";
import { FiHome, FiUsers } from "react-icons/fi";
import { IoSettingsOutline } from "react-icons/io5";
import { IoIosNotificationsOutline,IoMdAddCircle  } from "react-icons/io";
import { HiOutlineClipboardDocumentList } from "react-icons/hi2";
import { FaRegMessage } from "react-icons/fa6";
import { CgProfile } from "react-icons/cg";
import { CiLogout } from "react-icons/ci";
import { getAvatar } from "../../services/CustomerService";
import { getService, updatePrice, createService, activeService, deactiveService } from '../../services/EmployeeService';

const ServiceComponent = () => {
  const navigate = useNavigate();
  const [services, setServices] = useState([]);
  const [avatar, setAvatar] = useState(null);
  const [isDropdownOpen, setDropdownOpen] = useState(true);
  const [selectedService, setSelectedService] = useState(null);
  const [newPrice, setNewPrice] = useState('');
  const [showModal, setShowModal] = useState(false);
  
  const [showCreateModal, setShowCreateModal] = useState(false); 
  const [newServiceName, setNewServiceName] = useState('');
  const [newServicePrice, setNewServicePrice] = useState('');
 
  const [showActiveModal, setShowActiveModal] = useState(false); 
  const [isServiceDropdownOpen, setServiceDropdownOpen] = useState(false);

  const accountId = localStorage.getItem("accountId");

  useEffect(() => {
    const fetchAccount = async () => {
      try {
        const avatarUrl = await getAvatar(accountId);
        setAvatar(avatarUrl);
      } catch (error) {
        console.error("Error fetching account data:", error);
      }
    };
    getAllServices();
    if (accountId) fetchAccount();
  }, [accountId]);

  const getAllServices = () => {
    getService()
      .then((response) => {
        setServices(Array.isArray(response.data) ? response.data : []);

      })
      .catch((error) => console.error("Error fetching services: ", error));
  };

  const toggleServiceDropdown = () => {
    setServiceDropdownOpen(!isServiceDropdownOpen);
  };

  const handleOpenModal = (service) => {
    setSelectedService(service);
    setShowModal(true);
  };

  const handleCloseModal = () => {
    setShowModal(false);
    setNewPrice('');
  };

  const handleOpenCreateModal = () => {
    setShowCreateModal(true);
  };

  const handleCloseCreateModal = () => {
    setShowCreateModal(false);
    setNewServiceName('');
    setNewServicePrice('');
    
  };

  const handleOpenActiveModal = () => {
    setShowActiveModal(true);
    getAllServices(); 
  };

  const handleCloseActiveModal = () => {
    setShowActiveModal(false);
  };

  const handleUpdatePrice = () => {
    if (selectedService && newPrice) {
      updatePrice(selectedService.servicesId, newPrice)
        .then(() => {
          getAllServices();
          handleCloseModal();
        })
        .catch((error) => console.error("Error updating price:", error));
    }
  };

  const handleCreateService = () => {
    if (newServiceName && newServicePrice) {
      const service = { servicesName: newServiceName, price: newServicePrice };
      createService(service)
        .then(() => {
          getAllServices();
          handleCloseCreateModal();
        })
        .catch((error) => console.error("Error creating service:", error));
    }
  };
  const handleActivateService = (servicesId)=>{
    if(servicesId){
      activeService(servicesId)
      .then(()=>{
        getAllServices();
        
      })
      .catch((error) => console.error("Error creating service:", error));
    }
  }

  const handleDeactivateService = (servicesId)=>{
    if(servicesId){
      deactiveService(servicesId)
      .then(()=>{
        getAllServices();
        
      })
      .catch((error) => console.error("Error creating service:", error));
    }
  }

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const toggleDropdown = () => setDropdownOpen(!isDropdownOpen);

  return (
    <div className="container-fluid">
      <div className="row">
        <aside className="sidebar col-3 p-3">
          <div className='manager-sidebar'>
            <div className="profile-container text-center mb-4">
              <div className="SideKoi d-flex align-items-center justify-content-between">
                <img src="/Logo-Koi/Order.png" alt="Profile" className="profile-img rounded-circle" />
                <div className="KoiLogo">
                  <p className="KoiDeli">Koi Deli</p>
                </div>
              </div>
            </div>
            <nav>
            <ul className="list-unstyled">
            <div>
             
              <li>
                <a href="/"><i className="bi bi-speedometer2 me-2"><FiHome /></i> Homepage</a>
              </li>
            </div>
            
            <li>
              <a href="/manager"><i className="bi bi-person-badge me-2"><HiOutlineClipboardDocumentList /></i> Dashboard</a>
            </li>
            <li>
              <a href="/listcustomers"><i className="bi bi-people me-2"><FiUsers /></i>Danh sách nhân viên</a>
            </li>
            <li>
              <a href="/accounts"><i className="bi bi-person-badge me-2"><FiUsers /></i> Danh sách khách hàng</a>
            </li>

            <li onClick={toggleServiceDropdown} style={{ cursor: 'pointer', display: 'flex', alignItems: 'center' }}>
             < a href="#"><i className="bi bi-person-badge me-2"><FiUsers /></i> Quản lí dịch vụ</a>
            </li>
            {isServiceDropdownOpen && (
              <ul className="list-unstyled ms-3">
                <li>
                  <a href="#" onClick={handleOpenCreateModal}>
                    <i className="bi bi-person-badge me-2"><IoMdAddCircle /></i> Tạo mới
                  </a>
                </li>
                <li>
                  <a href="#" onClick={handleOpenActiveModal}>
                    <i className="bi bi-person-badge me-2"><IoMdAddCircle /></i> Kích hoạt
                  </a>
                </li>
              </ul>
            )}

            
            
<li>
            <a href="employee-page"><i className="bi bi-person-badge me-2"><CgProfile /></i>Thông tin tài khoản</a>
          </li>

          <li>
            <a onClick={handleLogout}><i className="bi bi-person-badge me-2"><CiLogout /></i>Đăng xuất</a>
          </li>

    
          </ul>
            </nav>
          </div>
        </aside>
        <main className="dashboard col-10">
          <header className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
            <h4 className="title">Dịch vụ</h4>
            <header className="d-flex justify-content-between align-items-center mb-4" style={{ marginRight: '50px' }}>
              <div className="header-content" style={{ width: '%' }}> 
                <div className="d-flex align-items-center justify-content-center search-container">
                  <input
                    className="search-bar"
                    type="text"
                    placeholder="Search Order"
                  />
                </div>
                <div className="navbar-cus-right">
                  <div className="dropdown" onClick={toggleDropdown}>
                    <img src={avatar || '/default-avatar.png'} alt="Avatar" className="avatar" />
                    {isDropdownOpen && ( 
                      <div className="dropdown-content">
                        <a href="user-page"><CgProfile /> Thông tin tài khoản</a>
                        <a onClick={handleLogout}><CiLogout /> Đăng xuất</a>
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
          <section className="delivery-ongoing-delivery mt-4 d-flex pt-3">
            <div className="delivery-list col-12">
              <table className="table table-striped table-bordered">
                <thead>
                  <tr>
                    <th>ServiceId</th>
                    <th>Dịch vụ</th>
                    <th>Giá</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                {services
                  .filter((service) => service.servicesStatus === 'online')
                  .map((service) => (
                    <tr key={service.servicesId}>
                      <td>{service.servicesId}</td>
                      <td>{service.servicesName}</td>
                      <td>${service.price}</td>
                      <td>
                        <button onClick={() => handleOpenModal(service)}>Cập nhật</button>
                      </td>
                    </tr>
                  ))}
              </tbody>
              </table>
            </div>
          </section>

          {/* Modal for updating price */}
          {showModal && (
            <div className="modal show d-block" role="dialog">
              <div className="modal-dialog" role="document">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5 className="modal-title">Cập nhật giá dịch vụ</h5>
                    <button type="button" className="close" onClick={handleCloseModal} aria-label="Close">
                      <span aria-hidden="true">&times;</span>
                    </button>
                  </div>
                  <div className="modal-body">
                    <input
                      type="number"
                      value={newPrice}
                      onChange={(e) => setNewPrice(e.target.value)}
                      placeholder="Enter new price"
                    />
                  </div>
                  <div className="modal-footer">
                    
                    <button type="button" className="btn btn-primary" onClick={handleUpdatePrice}>Lưu thay đổi</button>
                  </div>
                </div>
              </div>
            </div>
          )}

          {/* Modal for creating new service */}
          {showCreateModal && (
            <div className="modal show d-block" role="dialog">
              <div className="modal-dialog" role="document">
                <div className="modal-content">
                  <div className="modal-header">
                    <h5 className="modal-title">Tạo dịch vụ mới</h5>
                    <button 
                    type="button" 
                    className="close" 
                    onClick={handleCloseCreateModal} 
                    aria-label="Close" 
                    style={{ marginLeft: '270px', backgroundColor: 'whitesmoke', cursor: 'pointer' }}
                    >
                    <span aria-hidden="true" style={{  fontSize: '20px',color: 'black' }}>&times;</span>
                  </button>
                  </div>
                  <div className="modal-body">
                    <input
                      type="text"
                      value={newServiceName}
                      onChange={(e) => setNewServiceName(e.target.value)}
                      placeholder="Enter service name"
                    />
                    <input
                      type="number"
                      value={newServicePrice}
                      onChange={(e) => setNewServicePrice(e.target.value)}
                      placeholder="Enter service price"
                    />
                  </div>
                  <div className="modal-footer">
                    
                    <button type="button" className="btn btn-primary" onClick={handleCreateService}>Tạo</button>
                  </div>
                </div>
              </div>
            </div>
          )}

         {showActiveModal && (
          <div className="modal show d-block" role="dialog">
            <div className="modal-dialog" role="document">
              <div className="modal-content">
                <div className="modal-header">
                  <h5 className="modal-title">Quản lí dịch vụ</h5>
                  <button 
                    type="button" 
                    className="close" 
                    onClick={handleCloseActiveModal} 
                    aria-label="Close" 
                    style={{ marginLeft: '300px', backgroundColor: 'whitesmoke', cursor: 'pointer' }}
                    >
                    <span aria-hidden="true" style={{  fontSize: '20px',color: 'black' }}>&times;</span>
                  </button>
                </div>
                <div className="modal-body">
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>Tên dịch vụ</th>
                        <th>Giá</th>
                        <th>Trạng thái</th>
                        <th>Action</th>
                      </tr>
                    </thead>
                    <tbody>
                    {services
                    .sort((a, b) => {
                      
                      if (a.servicesStatus === 'online' && b.servicesStatus !== 'online') return -1;
                      if (a.servicesStatus !== 'online' && b.servicesStatus === 'online') return 1;
                  
                      return a.servicesId - b.servicesId;
                    })
                    .map((service) => (
                      <tr key={service.servicesId}>
                        <td>{service.servicesName}</td>
                        <td>${service.price}</td>
                        <td>{service.servicesStatus}</td>
                        <td>
                          {service.servicesStatus === 'online' ? (
                            <button
                              className="btn btn-danger btn-sm"
                              onClick={() => handleDeactivateService(service.servicesId)}
                            >
                              Vô hiệu hóa  
                            </button>
                          ) : (
                            <button
                              className="btn btn-success btn-sm"
                              onClick={() => handleActivateService(service.servicesId)}
                            >
                              Kích hoạt
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                    </tbody>
                  </table>
                </div>
             
              </div>
            </div>
          </div>
        )}

        </main>
      </div>
    </div>
  );
};

export default ServiceComponent;
