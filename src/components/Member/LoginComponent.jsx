import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import './Login.css';
import { loginAccount, googleLogin } from '../../services/EmployeeService';  
import { jwtDecode } from "jwt-decode";

const LoginComponent = ({ handleLogin }) => {
  const [userName, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const loginData = { userName, password }; 

    try {
      const response = await loginAccount(loginData);
      console.log("API Response:", response);

      const result = response.data;
      console.log("Role ID:", result.roleId);

      if (result.roleId === 'Manager') {
        navigate('/accounts');
      } else if (result.roleId === 'Delivery') {
        navigate('/delivery');
      } else if (result.roleId === 'Customer') {
        navigate('/customer');
      } else if (result.roleId === 'Manager') {
        navigate('/manager');
      }
      else if (result.roleId === 'Sales') {
        navigate('/salestaff');
      }
    } catch (error) {
      console.error('Login Error:', error);
      alert('An error occurred during login');
    }
  };

  const handleGoogleLoginSuccess = (response) => {
    console.log("Google login success:", response);
  
    const credential = response.credential;
    if (!credential) {
      console.error('No credential found in the response.');
      alert('Google login failed. No credential found.');
      return;
    }
 
    googleLogin(credential)
      .then((res) => {
        const data = res.data;
        console.log('Server response:', data); 
  
       
        if (data.success) {
          alert('Google login successful');
          handleLogin(true); 
  
          const roleId = data.roleId;
          
          switch (roleId) {
            case 'Manager':
              navigate('/accounts'); 
              break;
            case 'Delivery':
              navigate('/delivery'); 
              break;
            case 'Customer':
              navigate('/customer'); 
              break;
              case 'Sales':
              navigate('/customer'); 
              break;
            default:
              console.warn('Unknown roleId:', roleId);
              alert('Login successful, but unrecognized role.');
              break;
          }
  
        
        } else if (data.newAccount) {
          alert('New account has been created successfully');
          handleLogin(true); 
  
          const roleId = data.roleId;
          if (roleId === 'Customer') {
            navigate('/customer');
          }
  
        } else {
          
          alert('Login failed. Please check your details.');
        }
      })
      .catch((err) => {
        console.error('Error during Google login:', err);
        alert('An error occurred during Google login');
      });
  };
  
  const handleGoogleLoginFailure = (response) => {
    console.error('Google login failure:', response);
    alert('Google login failed, please try again.');
  };

  return (
    <div className="login-container">
      <div className='main-content'>
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
                  value={userName}
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
            </form>

            <GoogleLogin
              onSuccess={credentialResponse => {
                const decoded = jwtDecode(credentialResponse?.credential);
                console.log(decoded);
                handleGoogleLoginSuccess(credentialResponse);
              }}
              onError={() => {
                handleGoogleLoginFailure();
              }}
            />

            <div className="sign-up">
              Don't have an account? <a href="/register">Sign up now</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
