import axios from "axios";

const REST_API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/orders";


// API chỉ để cập nhật trạng thái
export const updateStatus = (orderId, newStatus) => {
  return axios.patch(`${REST_API_BASE_URL}/updateStatus/${orderId}`, { newStatus });
};


export const assignDriver = (orderId, deliver) => {
    return axios.patch(`${REST_API_BASE_URL}/update/${orderId}`, { deliver });
  };


const REST_API_BASE_URL4 = "http://koideliverysystem.id.vn:8080/api/accounts";

export const listAccount = () => {
  return axios.get(REST_API_BASE_URL4);
};
