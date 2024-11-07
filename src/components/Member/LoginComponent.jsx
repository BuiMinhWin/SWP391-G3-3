import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { GoogleLogin } from '@react-oauth/google';
import ReCAPTCHA from 'react-google-recaptcha'; // Import reCAPTCHA
import './Login.css';
import { loginAccount } from '../../services/EmployeeService';
import { useSnackbar } from 'notistack';

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
      enqueueSnackbar('Đăng nhập thất bại. Vui lòng thử lại.', { variant: 'error' });
    }
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
              onSuccess={() => console.log('Google login success')}
              onError={() => console.log('Google login error')}
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
