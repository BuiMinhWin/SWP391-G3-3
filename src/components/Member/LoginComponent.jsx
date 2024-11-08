import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import ReCAPTCHA from 'react-google-recaptcha'; // Import reCAPTCHA
import './Login.css';
import { loginAccount , googleLogin} from '../../services/EmployeeService';
import { useSnackbar } from 'notistack';
import * as jwtJsDecode from 'jwt-js-decode';

const LoginComponent = () => {
  const [userName, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [recaptchaValidated, setRecaptchaValidated] = useState(false); // Track reCAPTCHA status
  const { enqueueSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const recaptchaSiteKey = import.meta.env.VITE_RECAPTCHA_SITE_KEY;

  const handleSubmit = async (e) => {
    e.preventDefault();
  
    if (!recaptchaValidated) {
      enqueueSnackbar('Vui lòng xác nhận reCAPTCHA.', { variant: 'error' });
      return;
    }
  
    const loginData = { userName, password };
  
    try {
      const response = await loginAccount(loginData);
      const result = response?.data;
  
      if (!result) {
        enqueueSnackbar('Đăng nhập thất bại. Không tìm thấy dữ liệu.', { variant: 'error' });
        return;
      }
  
      localStorage.setItem('roleId', result.roleId);
      localStorage.setItem('accountId', result.accountId);
  
      enqueueSnackbar('Đăng nhập thành công', { variant: 'success' });
  
      switch (result.roleId) {
        case 'Manager':
          navigate('/manager');
          break;
        case 'Delivery':
          navigate('/delivery');
          break;
        case 'Customer':
          navigate('/customer');
          break;
        case 'Sales':
          navigate('/salestaff');
          break;
        default:
          enqueueSnackbar('Vai trò không xác định, vui lòng liên hệ hỗ trợ.', { variant: 'warning' });
      }
    } catch (error) {
      if (error.response && error.response.data) {
       
        enqueueSnackbar(error.response.data.message || 'Đăng nhập thất bại. Vui lòng thử lại.', {
          variant: 'error',
        });
      } else {
        enqueueSnackbar('Đăng nhập thất bại. Vui lòng thử lại.', { variant: 'error' });
      }
    }
  };
  
  
  const handleGoogleLoginSuccess = async (response) => {
    const credential = response.credential;
    if (!credential) {
      enqueueSnackbar('Google login failed. No credential found.', { variant: 'error', autoHideDuration: 1000 });
      return;
    }

    try {
      const decodedToken = jwtJsDecode.decode(credential);
      const { payload } = decodedToken;
      console.log("Decoded payload:", payload);

      const { given_name: firstName, family_name: lastName, email, picture: avatarUrl } = payload;
      console.log("Email from payload:", email);

      // const avatarBytes = await convertAvatarUrlToBytes(avatarUrl);
      // const avatar = avatarBytes ? btoa(String.fromCharCode(...avatarBytes)) : null;

      const account = {
        firstName,
        lastName: lastName || 'Unknown',  
        userName: userName || email,       
        password,
        email,
        roleId: "Customer",
        createAt: localStorage.getItem('createAt') || new Date().toISOString(),
      };
      
      // Kiểm tra các trường bắt buộc
      if (!account.lastName || !account.userName || !account.email) {
        enqueueSnackbar('Thông tin không đầy đủ, vui lòng thử lại.', { variant: 'error' });
        return;
      }

      const response = await googleLogin(account);
      console.log("Google login response:", response);
      const result = response?.data;

      if (!result) {
        enqueueSnackbar('Failed to retrieve data from Google login response.', { variant: 'error' });
        return;
      }

      localStorage.setItem('createAt', account.createAt);
      localStorage.setItem('roleId', result.roleId);
      localStorage.setItem('accountId', result.accountId);

      if (result.roleId === 'Customer') {
        enqueueSnackbar('Google login successful', { variant: 'success', autoHideDuration: 1000 });
        navigate('/customer');
      } else {
        enqueueSnackbar('Unexpected roleId. Please try again.', { variant: 'warning', autoHideDuration: 1000 });
      }
    } catch (error) {
      console.error('Error during Google login:', error);
      enqueueSnackbar('An error occurred during Google login', { variant: 'error', autoHideDuration: 1000 });
    }
  };
  const handleGoogleLoginFailure = () => {
    enqueueSnackbar('Google login failed, please try again.', { variant: 'error', autoHideDuration: 1000 });
  };

  const handleRecaptchaChange = (token) => {
    if (token) {
      setRecaptchaValidated(true); // Mark as validated
    } else {
      setRecaptchaValidated(false);
    }
  };

  return (
    <div className="login-container">
      <div className="main-content">
        <div className="login-image-container"></div>
        <div className="login-form-container">
          <div className="login-form-box">
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
                  placeholder="Your username"
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
              <div className="recaptcha-container">
              <ReCAPTCHA
                sitekey={recaptchaSiteKey}
                onChange={handleRecaptchaChange}
              />
               {/* <a href="/reset">Forgot password?</a> */}
            </div>
              <button type="submit">Sign In</button>
              
            </form>
           
            <GoogleLogin
              clientId={import.meta.env.VITE_CLIENT_ID}
              onSuccess={handleGoogleLoginSuccess}
              onError={handleGoogleLoginFailure}
            />

            <div className="sign-up">
              Don't have an account? <a href="/register">Đăng kí</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginComponent;
