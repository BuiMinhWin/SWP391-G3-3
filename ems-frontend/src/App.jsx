import { useState } from 'react';
import './App.css';
import ListEmployeeComponent from './components/ListEmployeeComponent';
import FooterComponent from './components/FooterComponent';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import EmployeeComponent from './components/EmployeeComponent';
import LoginComponent from './components/LoginComponent';
import RegisterComponent from './components/RegisterComponent'; 
import DeliveryComponent from './components/DeliveryComponent';

function App() {

  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleLogin = (status) => {
    setIsAuthenticated(status);
  };

  return (
    <div id="app-container">
      <BrowserRouter>
        <div className="main-content">
          <Routes>
            <Route path='/delivery' element={<DeliveryComponent />} />

            <Route path='/login' element={<LoginComponent handleLogin={handleLogin} />} />
            <Route path="/register" element={<RegisterComponent />} />
            <Route path='/' element={isAuthenticated ? <ListEmployeeComponent /> : <Navigate to='/login' />} />
            <Route path='/employees' element={isAuthenticated ? <ListEmployeeComponent /> : <Navigate to='/login' />} />
            <Route path='/add-employee' element={isAuthenticated ? <EmployeeComponent /> : <Navigate to='/login' />} />
            <Route path='/edit-employee/:id' element={isAuthenticated ? <EmployeeComponent /> : <Navigate to='/login' />} />
          </Routes>
        </div>
        <FooterComponent />
      </BrowserRouter>
    </div>
  );
}

export default App;
