import React, { useEffect, useState } from 'react';
import { deleteAccount, listAccount } from '../../services/EmployeeService';
import { useNavigate } from 'react-router-dom';
import './ListEmployee.css';
import {  getAvatar} from "../../services/CustomerService";  
import { FaRegCalendarAlt } from "react-icons/fa";
import { FiHome ,FiUsers } from "react-icons/fi";
import { IoSettingsOutline } from "react-icons/io5";
import { MdSupportAgent} from "react-icons/md";
import { IoIosNotificationsOutline,IoMdAddCircle  } from "react-icons/io";
import { HiOutlineClipboardDocumentList } from "react-icons/hi2";

import { FaRegMessage } from "react-icons/fa6";
import { CgProfile } from "react-icons/cg";
import { CiLogout } from "react-icons/ci";

const ListEmployeeComponent = () => {
  const [accounts, setAccounts] = useState([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [avatar, setAvatar] = useState(null); 
  const navigate = useNavigate();
  const accountId = localStorage.getItem("accountId");
  const [roleIdFilter, setRoleIdFilter] = useState('');

  const filteredAccounts = accounts.filter(account => {
    const matchesRoleId = roleIdFilter ? account.roleId.includes(roleIdFilter) : true;
    const matchesSearchTerm = 
      account.firstName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      account.lastName.toLowerCase().includes(searchTerm.toLowerCase()) ||
      account.email.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesRoleId && matchesSearchTerm;
  });

  
  const [isDropdownOpen, setDropdownOpen] = useState(true); //drop down
  const toggleDropdown = () => {
    setDropdownOpen(!isDropdownOpen);
  }
  const handleLogout = () => {
    logout(); 
    navigate('/'); 
  };
  
  useEffect(() => {
    getAllAccounts();
    fetchAccount();
  }, []);

  
  const fetchAccount = async () => {
    try {
    
      const avatarUrl = await getAvatar(accountId);
      setAvatar(avatarUrl);
    } catch (error) {
      console.error("Error fetching account data:", error);
    } 
  };

  const getAllAccounts = () => {
    listAccount()
      .then((response) => {
        if (Array.isArray(response.data)) {
          const employeeAccounts = response.data.filter(account => (account.roleId !== 'Customer' && account.roleId !== 'Manager'));
          setAccounts(employeeAccounts);
        } else {
          console.error("API response is not an array", response.data);
          setAccounts([]);
        }
      })
      .catch((error) => {
        console.error("Error fetching employees: ", error);
      });
  };

  const addNewAccount = () => {
    navigate('/add-account');
  };

  const updateAccount = (accountId) => {
    navigate(`/edit-account/${accountId}`);
  };

  const removeAccount = (accountId) => {
    deleteAccount(accountId)
      .then(() => {
        getAllAccounts();
      })
      .catch((error) => {
        console.error("Error deleting employee: ", error);
      });
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };


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
         
            
          </div>
          <nav>
      <ul className="list-unstyled">
        <h6>Main</h6>
        <li>
            <a href="/"><i className="bi bi-speedometer2 me-2"> <FiHome /> </i>  Homepage</a>
        </li>

        
        <h6>List</h6>
        <li>
          <a href="/manager"><i className="bi bi-person-badge me-2"><HiOutlineClipboardDocumentList /></i>Dashboard</a>
        </li>

        <li>
          <a  href="/listcustomers"><i className="bi bi-people me-2"><FiUsers /></i> Customers</a>
        </li>

        <li>
          <a href="/accounts"><i className="bi bi-person-badge me-2"><FiUsers /></i> Employees</a>
        </li>

         <li>
            <a href="/service"><i className="bi bi-person-badge me-2"><HiOutlineClipboardDocumentList /></i> Services</a>
          </li>
        
        <h6>General</h6>
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
      <header className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2 ">
        <h4 className="title">Manage Account</h4>
            <header className="d-flex justify-content-between align-items-center mb-4 ">
           
            <div className="search-bar ml-auto">
              <input
                 type="text"
                 className="form-control"
                 placeholder="Search..."
                 value={searchTerm}
                 onChange={handleSearchChange}
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
            

            

            <div className="notification-icon m-4">
                <IoIosNotificationsOutline />
                
            </div>
          </header>

          </header>
        <div className="d-flex align-items-left mb-3">
       
     
       </div>
       <section className="delivery-ongoing-delivery mt-4 d-flex pt-3">
        <div className="delivery-list col-12">
          <div className="filter-bar d-flex mb-4 col-10 align-items-center">
            <select className="form-select me-2 col-4" value={roleIdFilter} onChange={(e) => setRoleIdFilter(e.target.value)}>
              <option value="">All Employees</option>
              <option value="Sales">Sales</option>
              <option value="Delivery">Delivery</option>
            </select>
            
            <div className="ms-auto">
            <button className="add-btn" onClick={addNewAccount}>
              <IoMdAddCircle style={{ color: 'white' }} />
          </button>
            </div>
          </div>

          <table className="table table-striped table-bordered">
            <thead>
              <tr>
                <th>Account Id</th>
                <th>RoleID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Email</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredAccounts.length > 0 ? (
                filteredAccounts.map(account => (
                  <tr key={account.accountId}>
                    <td>{account.accountId}</td>
                    <td>{account.roleId}</td>
                    <td>{account.firstName}</td>
                    <td>{account.lastName}</td>
                    <td>{account.email}</td>
                    <td>
                      <button className="btn btn-info" onClick={() => updateAccount(account.accountId)}>Update</button>
                      <button className="btn btn-danger" onClick={() => removeAccount(account.accountId)}>Delete</button>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center">No Employees Found</td>
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

export default ListEmployeeComponent;
