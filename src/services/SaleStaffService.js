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

export const getOrderDetail =(orderId) => {
  return axios.get(`${REST_API_BASE_URL2}/${orderId}`);
};



// API chỉ để cập nhật trạng thái
export const updateStatus = (orderId, newStatus) => {
  return axios.patch(`${REST_API_BASE_URL}/updateStatus/${orderId}`, { newStatus });
};

// API để cập nhật accountId vào cột sale
export const updateSale = (orderId, sale) => {
  return axios.patch(`${REST_API_BASE_URL}/update/${orderId}`, { sale });
};

export const assignDriver = (orderId, deliver) => {
  return axios.patch(`${REST_API_BASE_URL}/update/${orderId}`, { deliver });
};


const REST_API_BASE_URL3 = "http://koideliverysystem.id.vn:8080/api/documents/download/order/{orderId}";
export const getDocument = (orderId) => {
  return axios.get(`${REST_API_BASE_URL3}/${orderId}`);
}

export const getDrivers = () => {
  return axios.get(`${REST_API_BASE_URL}/deliverers`);
};



const REST_API_BASE_URL4 = "http://koideliverysystem.id.vn:8080/api/accounts";

export const listAccount = () => {
  return axios.get(REST_API_BASE_URL4);
};
