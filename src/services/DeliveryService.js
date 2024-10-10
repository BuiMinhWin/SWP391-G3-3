import axios from "axios";

const REST_API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/orders";
const REST_API_BASE_URL2 = "http://koideliverysystem.id.vn:8080/api/ordersDetail/order";
export const getOrder = (orderId) => {
  return axios.get(REST_API_BASE_URL + '/' + orderId);
}

export const listOrder = () => {
  return axios.get(REST_API_BASE_URL);
};

export const trackingOrder = (orderId) => {
  return axios.post(`${REST_API_BASE_URL}/update`, orderId);
} 

export const getOrderDetail = (orderId)=>{
  return axios.get(REST_API_BASE_URL2+ '/' + orderId)
}

export const updateStatus = (orderId, status) => {
  return axios.put(`${REST_API_BASE_URL}/updateStatus/${orderId}`, { status });
};
