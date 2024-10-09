import { Navigate } from "react-router-dom";
import logo from "../../../assets/Logo.png";

const HeaderBar = () => {
  return (
    <div>
      <div className="navbar-left">
        <img src={logo} className="logo" alt="Logo" />
        <a className="nav-link" onClick={() => Navigate("/")}>
          Trang Chủ
        </a>
        {/* Dropdown Dịch Vụ */}
        <div className="dropdown">
          <a href="#" className="nav-link">
            Dịch Vụ
          </a>
          <div className="dropdown-content">
            <a href="/form">Tạo Đơn</a>
            <a href="#">Ước Tính Chi Phí</a>
            <a href="#">Theo dõi đơn hàng</a>
            <a href="#">Quy định vận chuyển</a>
            <a href="#">Chương trình khuyến mãi</a>
          </div>
        </div>
        <a href="#" className="nav-link">
          Giới Thiệu
        </a>
      </div>
      <div className="navbar-right">
        <a href="#" className="nav-link support-link">
          <i className="fas fa-question-circle"></i>Hỗ Trợ
        </a>
        <button className="register-btn" onClick={() => Navigate("/register")}>
          Đăng Ký
        </button>
        <button className="login-btn" onClick={() => Navigate("/login")}>
          Đăng Nhập
        </button>
      </div>
    </div>
  );
};
export default HeaderBar;
