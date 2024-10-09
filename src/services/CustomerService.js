import axios from "axios";

const REST_API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/orders";

export const createOrder = (orderData) => {
    return axios.post(`${REST_API_BASE_URL}/create`, orderData);
  };

export const orderDetail = (orderDetail) => {
  return axios.post(`${REST_API_BASE_URL}/create`, orderDetail);
}

export const orderDocument = (orderDocument) => {
  return axios.post(`${REST_API_BASE_URL}/create`, orderDocument);
}
