import React from "react";
import { Route, Routes } from "react-router-dom";
import Homepage from "../pages/HomePage/HomePage";
import LoginComponent from "../components/Member/LoginComponent";
import RegisterComponent from "../components/Member/RegisterComponent";
import UserPage from "../pages/UserPage/UserPage";
import OrderForm from "../components/OrderForm/OrderForm";
import SaleStaffComponent from "../components/SaleStaff/SaleStaffComponent";
import DeliveryComponent from "../components/DeliveryStaff/DeliveryComponent";
import CustomerHomePage from "../components/Customer/CustomerHomePage";
import ManagerComponent from "../components/Manager/ManagerComponent";
import ListOrderComponent from "../components/DeliveryStaff/ListOrderComponent";
import EmployeeComponent from "../components/Manager/EmployeeComponent";

import ListEmployeeComponent from "../components/Manager/ListEmployeeComponent";
import ListCustomerComponent from "../components/Customer/ListCustomerComponent";
import OrderDetailComponent from "../components/DeliveryStaff/OrderDetailComponent";
// import { OrderProvider } from '../components/DeliveryStaff/OrderContext';
import CheckoutPage from "../pages/CheckoutPage/CheckoutPage";
import ListOrderOfSales from "../components/SaleStaff/ListOrderOfSales";
import Blog from "../pages/Blog/Blog";
import Layout from "../components/Layout/Layout";
import OrderDetailDocumentComponent from '../components/SaleStaff/OrderDetailDocumentComponent';
import ListOfConfirmOrder from '../components/SaleStaff/ListOfConfirmOrder'
import OrderReport from "../pages/Order/OrderReport";

function index() {
  return (
    // <OrderProvider>
    <Routes>
      <Route path="/" element={<Homepage />} />
      <Route path="/login" element={<LoginComponent />} />
      <Route path="/user-page" element={<UserPage />} />
      <Route path="/register" element={<RegisterComponent />} />

      <Route path="/" element={<Layout />}>
        <Route path="/user" element={<UserPage />} />
        <Route path="/form" element={<OrderForm />} />
        <Route path="/checkout" element={<CheckoutPage />} />
        <Route path="/order-report" element={<OrderReport />} />
      </Route>

      <Route path="/blog" element={<Blog />} />

      <Route path="/manager" element={<ManagerComponent />} />
      <Route path="/accounts" element={<ListEmployeeComponent />} />
      <Route path="/add-account" element={<EmployeeComponent />} />
      <Route path="/edit-account/:accountId" element={<EmployeeComponent />} />
      <Route path="/listcustomers" element={<ListCustomerComponent />} />

      <Route path="/delivery" element={<DeliveryComponent />} />
      <Route path="/customer" element={<CustomerHomePage />} />
      <Route path="/orders" element={<ListOrderComponent />} />
      <Route path="/order/:orderId" element={<OrderDetailComponent />} />

      {/*Sales */}
      <Route path="/salestaff/listsaleorder" element={<ListOrderOfSales />} />
      <Route path="/salestaff" element={<SaleStaffComponent />} />
      <Route path="/confirm" element={<ListOfConfirmOrder />} />
      <Route path="/confirmDetail/:orderId" element={<OrderDetailDocumentComponent />} />
    
    </Routes>
  );
}

export default index;

//rfce enter
