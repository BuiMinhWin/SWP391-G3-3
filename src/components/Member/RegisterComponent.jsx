import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Register.css';
import { createAccount } from '../../services/EmployeeService';

const RegisterComponent = () => {
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [userName, setUserName] = useState(''); 
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [email, setEmail] = useState('');
  const [phone, setPhone] = useState('');
  const [avatar, setAvatar] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (e) => {
    console.log(account)
    e.preventDefault();

    if (password !== confirmPassword) {
      alert('Passwords do not match!');
      return;
    }

    const account = { firstName, lastName, userName, password, email, phone, roleId: 'Customer', avatar, createAt: new Date().toISOString };

    createAccount(account)
      .then((response) => {
        console.log('Account created:', response.data);
        navigate('/login');
      });
  };

  return (
    <div class="background-section">
    <div className="register-container">
      <div className="background-giphy"></div>
      <div className="register-form-container">
        <div className="form-box">
          <h2>Create Your Account</h2>
          <form onSubmit={handleSubmit}>
            <div className="input-group">
              <label>First Name</label>
              <input 
                type="text" 
                value={firstName} 
                onChange={(e) => setFirstName(e.target.value)} 
                required 
                placeholder="Your First Name"
              />
            </div>
            <div className="input-group">
              <label>Last Name</label> 
              <input 
                type="text"  
                value={lastName} 
                onChange={(e) => setLastName(e.target.value)} 
                required 
                placeholder="Your Last Name"
              />
            </div>
            <div className="input-group">
              <label>Username</label>
              <input 
                type="text" 
                value={userName} 
                onChange={(e) => setUserName(e.target.value)}  
                required 
                placeholder="Username"
              />
            </div>
            <div className="input-group">
              <label>Password</label>
              <input 
                type="password" 
                value={password} 
                onChange={(e) => setPassword(e.target.value)} 
                required 
                placeholder="Enter password"
              />
            </div>
           <div className="input-group">
              <label>Confirm Password</label>
              <input 
                type="password" 
                value={confirmPassword} 
                onChange={(e) => setConfirmPassword(e.target.value)} 
                required 
                placeholder="Confirm password"
              />
            </div> 
            <div className="input-group">
              <label>Email</label>
              <input 
                type="email" 
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
                required 
                placeholder="Email"
              />
            </div>
            <div className="input-group">
              <label>Phone Number</label>
              <input 
                type="tel" 
                value={phone} 
                onChange={(e) => setPhone(e.target.value)} 
                required 
                placeholder="Phone Number"
              />
            </div>
            {/* <div className="input-group">
              <label>Avatar</label>
              <input 
                type="file" 
                multiple
                accept="image/*"
                onChange={(e) => setAvatar(e.target.files)} 
              />
              {avatar && [...avatar].map((ava, index) => (
                <div key={index} className="avatar-preview">
                  <img src={URL.createObjectURL(ava)} width="100px" alt={`Avatar ${index}`} />
                </div>
              ))}
            </div> */}
            <button type="submit" className="submit-btn">Sign Up</button>
            <button type="button" className="google-login">Sign Up with Google</button>
          </form>
        </div>
      </div>
    </div>
    </div>

  );
};

export default RegisterComponent;
