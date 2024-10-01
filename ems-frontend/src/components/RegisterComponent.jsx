import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../App.css';
import { createAccount } from '../services/EmployeeService';

const RegisterComponent = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [userName, setUserName] = useState(''); 
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    e.preventDefault();

   
    if (password !== confirmPassword) {
      alert('Passwords do not match!');
      return;
    }
  
    const account = { firstName, lastName, userName, password, email, phone,roleId: 'Customer',avatar:"",createAt:new Date().toISOString };

    createAccount(account)
      .then((response) => {
        console.log('Account created:', response.data);
        
        navigate('/login');
      })
      
  };

  return (
    <div className="register-container">
      <div className="background-giphy"></div>
      <div className="register-form-container">
        <div className="form-box">
          <h2>Register</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <label>First Name</label>
              <input 
                type="text" 
                value={firstName} 
                onChange={(e) => setFirstName(e.target.value)} 
                required 
                placeholder="Your First Name"
              />
            </div>
            <div>
              <label>Last Name</label> 
              <input 
                type="text"  
                value={lastName} 
                onChange={(e) => setLastName(e.target.value)} 
                required 
                placeholder="Your Last Name"
              />
            </div>
            <div>
              <label>Username</label>
              <input 
                type="text" 
                value={userName} 
                onChange={(e) => setUserName(e.target.value)}  
                required 
                placeholder="Username"
              />
            </div>
            <div>
              <label>Password</label>
              <input 
                type="password" 
                value={password} 
                onChange={(e) => setPassword(e.target.value)} 
                required 
                placeholder="Enter password"
              />
            </div>
            <div>
              <label>Confirm Password</label>
              <input 
                type="password" 
                value={confirmPassword} 
                onChange={(e) => setConfirmPassword(e.target.value)} 
                required 
                placeholder="Confirm password"
              />
            </div>
            <div>
              <label>Email</label>
              <input 
                type="email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                required 
                placeholder="Email"
              />
            </div>
            <div>
              <label>Phone Number</label>
              <input 
                type="tel" 
                value={phone} 
                onChange={(e) => setPhone(e.target.value)} 
                required 
                placeholder="Phone Number"
              />
            </div>
            <button type="submit">Sign Up</button>
            <button type="button" className="google-login">Sign Up with Google</button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterComponent;
