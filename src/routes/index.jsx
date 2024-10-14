import React from 'react'
import { Route, Routes } from 'react-router-dom'
import Homepage from '../pages/HomePage/HomePage'
import LoginComponent from '../components/Member/LoginComponent'
import RegisterComponent from '../components/Member/RegisterComponent'
import UserPage from '../pages/UserPage/UserPage'
import OrderForm from '../components/OrderForm/OrderForm'
import SaleStaffComponent from '../components/SaleStaff/SaleStaffComponent'
import DeliveryComponent from '../components/DeliveryStaff/DeliveryComponent'
import CustomerHomePage from '../components/Customer/CustomerHomePage'
import ManagerComponent from '../components/Manager/ManagerComponent'
import ListEmployeeComponent from '../components/Manager/ListEmployeeComponent'
import ListOrderComponent from '../components/DeliveryStaff/ListOrderComponent'
import ProfileComponent from '../components/Member/ProfileComponent'

import ListOrderOfSales from '../components/SaleStaff/ListOrderOfSales'
import ReportPage from '../components/SaleStaff/ReportPage'
import FeedbackPage from '../components/SaleStaff/FeedbackPage'
import Blog from '../pages/Blog/Blog'

function index() {
  return (
    <Routes>
        <Route path='/' element={<Homepage/>} />
        <Route path='/login' element={<LoginComponent/>} />
        <Route path='/accounts' element={<ListEmployeeComponent/>}/>
        <Route path='/orders' element={<ListOrderComponent/>}/>
        <Route path='/register' element={<RegisterComponent/>} />
        <Route path='/user' element={<UserPage/>}/>
        <Route path='/form' element={<OrderForm/>}/>
        <Route path='/manager' element={<ManagerComponent/>} />
        <Route path='/salestaff' element={<SaleStaffComponent/>} />
        <Route path='/delivery' element={<DeliveryComponent/>} /> 
        {/* <Route path='/profile' element={<ProfileComponent/>} /> để sau  */}
        <Route path='/customer' element={<CustomerHomePage/>} /> 
        <Route path='/manager' element={<ManagerComponent/>} /> 
        <Route path='/user-page' element={<UserPage/>}/>
        <Route path='/blog' element = {<Blog/>}/>

        {/*Sales */}
        <Route path="/salestaff/listsaleorder" element={<ListOrderOfSales />} />
          
          <Route path="/salestaff/reports" element={<ReportPage />} />
          <Route path="/salestaff/feedback" element={<FeedbackPage />} />
             
    </Routes>

   


  )

}

export default index

//rfce enter