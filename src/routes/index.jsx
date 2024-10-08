import React from 'react'
import { Route, Routes } from 'react-router-dom'
import Homepage from '../pages/HomePage/HomePage'
import LoginComponent from '../components/Member/LoginComponent'
import RegisterComponent from '../components/Member/RegisterComponent'
import UserPage from '../pages/UserPage/UserPage'
import OrderForm from '../components/OrderForm/OrderForm'

function index() {
  return (
    <Routes>
        <Route path='/' element={<Homepage/>} />
        <Route path='/login' element={<LoginComponent/>} />
        <Route path='/register' element={<RegisterComponent/>} />
        <Route path='/user' element={<UserPage/>}/>
        <Route path='/form' element={<OrderForm/>}/>

        {/* <Route path='/' element={
        <Navigate to='/home' relative={true} /> */}
    {/* } /> */}
        
    </Routes>

   


  )

}

export default index

//rfce enter