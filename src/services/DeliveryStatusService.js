import axios from 'axios';

const API_BASE_URL = 'http://koideliverysystem.id.vn:8080/api/deliveryStatus';

export const getAllDeliveryStatusByOrderId = (orderId) => {
  return axios.get(`${API_BASE_URL}/getAllDeliveryStatusByOrderId/${orderId}`);
};