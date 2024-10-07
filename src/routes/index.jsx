import React from 'react'
import { Route, Routes } from 'react-router-dom'
import Homepage from '../pages/HomePage/HomePage'
import LoginComponent from '../components/Member/LoginComponent'
import RegisterComponent from '../components/Member/RegisterComponent'

function index() {
  return (
    <Routes>
        <Route path='/' element={<Homepage/>} />
        <Route path='/login' element={<LoginComponent/>} />
        <Route path='/register' element={<RegisterComponent/>} />

        {/* <Route path='/' element={
        <Navigate to='/home' relative={true} /> */}
    {/* } /> */}
        
    </Routes>

   


  )

}

export default index

//rfce enter