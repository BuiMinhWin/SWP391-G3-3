import axios from "axios";

const REST_API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/orders";
const REST_API_BASE_URL2 = "http://koideliverysystem.id.vn:8080/api/accounts";

export const createOrder = async (orderData) => {
  try {
    const response = await axios.post(`${REST_API_BASE_URL}/create`, orderData);
    return response.data; // Return the data from the response
  } catch (error) {
    console.error("Error creating order:", error.response || error.message);
    throw error; // Propagate error to be handled by the caller
  }
};

export const getAccountById = async (accountId) => {
  try {
    const response = await axios.get(`${REST_API_BASE_URL2}/${accountId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching account:", error);
    throw error;
  }
};

// Update account details
export const updateAccount = async (accountId, accountData) => {
  try {
    const response = await axios.put(`${REST_API_BASE_URL2}/${accountId}`, accountData);
    return response.data;
  } catch (error) {
    console.error("Error updating account:", error);
    throw error;
  }
};


const REST_API_BASE_URL3 = "http://koideliverysystem.id.vn:8080/api/orders";

export const getOrder = (orderId) => {
  return axios.get(REST_API_BASE_URL3 + '/' + orderId);
}