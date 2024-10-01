import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom'; 
import '../App.css'; 

const LoginComponent = ({ handleLogin }) => {
  const [userName, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate(); 

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    const loginData = { userName, password };
  
    try {
      const response = await fetch('http://localhost:8080/api/accounts/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(loginData),
      });
  
      if (response.ok) {
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          const result = await response.json();
  
          if (result.roleId === 'admin') {
            alert('Welcome Admin!');
            handleLogin(true);
            navigate('/accounts'); 
          } 
          if (result.roleId ==="Delivering Staff"){
            alert("Welcome");
            handleLogin(true);
            navigate("/delivery");
          }
        } else {
          alert('Unexpected response format');
        }
      } else {
        alert('Login failed');
      }
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred');
    }
  };

  return (
    <div className="login-container">
      <div className="login-image-container"></div>
      <div className="login-form-container">
        <div className="form-box">
          <div className="logo-title">
            <img src="/Logo-Koi/Order.png" alt="Koi Logo" className="koi-logo" />
            <div className="text-container">
              <h2>Koi Delivery</h2>
              <p>Nice to see you again</p>
            </div>
          </div>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Username</label>
              <input 
                type="text" 
                value={userName } 
                onChange={(e) => setUsername(e.target.value)} 
                required 
                placeholder="Email or phone number"
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
            <div className="options">
              <div className="remember-me"> 
                <input type="checkbox" id="remember" />
                <label htmlFor="remember">Remember me</label>
              </div>
              <a href="#">Forgot password?</a>
            </div>
            <button type="submit">Sign In</button>
            <button type="button" className="google-login"> Sign in with Google</button>
          </form>
          <div className="sign-up">
            Don't have an account? <a href="/register">Sign up now</a>
          </div>

        </div>
      </div>
    </div>
  );
};

export default LoginComponent;