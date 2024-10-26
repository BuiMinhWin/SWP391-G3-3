import axios from "axios";

const REST_API_ORDER_URL = "http://koideliverysystem.id.vn:8080/api/orders";
const REST_API_ORDER_DETAIL_URL =
  "http://koideliverysystem.id.vn:8080/api/ordersDetail";
const REST_API_DOCUMENT_URL =
  "http://koideliverysystem.id.vn:8080/api/documents";
const REST_API_ACCOUNT_URL = "http://koideliverysystem.id.vn:8080/api/accounts";

export const createOrder = async (orderData) => {
  try {
    const response = await axios.post(
      `${REST_API_ORDER_URL}/create`,
      orderData
    );
    return response.data;
  } catch (error) {
    console.error("Error creating order:", error.message); // Log only the message
    if (error.response) {
      console.error("Response data:", error.response.data);
      console.error("Response status:", error.response.status);
      console.error("Response headers:", error.response.headers);
    } else if (error.request) {
      console.error("Request data:", error.request);
    }
    throw error;
  }
};

export const uploadDocument = async (file, orderId) => {
  const formData = new FormData();
  formData.append("document_file", file);
  formData.append("orderId", orderId); // Attach orderId to the form data

  try {
    const response = await axios.post(
      `${REST_API_DOCUMENT_URL}/${orderId}`,
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    );
    return response.data;
  } catch (error) {
    console.error("File upload failed:", error);
    throw new Error("Upload failed");
  }
};

export const createOrderDetail = async (orderDetailData) => {
  try {
    const response = await axios.post(
      `${REST_API_ORDER_DETAIL_URL}/create`,
      orderDetailData
    );
    return response.data;
  } catch (error) {
    console.error("Error creating order:", error.response || error.message);
    throw error;
  }
};
export const order = async (orderId) => {
  try {
    const response = await axios.get(`${REST_API_ORDER_URL}/${orderId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching order data:", error);
    throw error;
  }
};
export const orderDetail = async (orderId) => {
  try {
    const response = await axios.get(
      `${REST_API_ORDER_DETAIL_URL}/order/${orderId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching order data:", error);
    throw error;
  }
};

const REST_API_BASE_URL3 = "http://koideliverysystem.id.vn:8080/api/orders";

export const getOrder = (orderId) => {
  return axios.get(REST_API_BASE_URL3 + "/" + orderId);
};

export const cancelOrder = async (orderId) => {
  const response = await fetch(`${REST_API_ORDER_URL}/${orderId}`, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error("Failed to cancel order ");
  }
  return response.status !== 204 ? response.json() : {};
};

export const getOrderPDF = async (orderId) => {
  const response = await fetch(
    `${REST_API_DOCUMENT_URL}/download/order/${orderId}`
  );
  if (!response.ok) {
    throw new Error("Failed to fetch PDF");
  }
  return response.blob();
};

export const getAccountById = async (accountId) => {
  try {
    const response = await axios.get(`${REST_API_ACCOUNT_URL}/${accountId}`);
    return response.data;
  } catch (error) {
    console.error("Error fetching account:", error);
    throw error;
  }
};

export const updateAccount = async (accountId, accountData) => {
  const response = await fetch(`${REST_API_ACCOUNT_URL}/update/${accountId}`, {
    method: "PATCH",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(accountData),
  });

  if (!response.ok) {
    throw new Error("Failed to update account.");
  }

  return response.json();
};

export const getAvatar = async (accountId) => {
  const response = await fetch(`${REST_API_ACCOUNT_URL}/${accountId}/avatar`);
  if (!response.ok) throw new Error("Failed to fetch account Avatar");
  const blob = await response.blob();
  return URL.createObjectURL(blob);
};

export const updateAvatar = async (accountId, avatarFile) => {
  const formData = new FormData();
  formData.append("avatar", avatarFile);

  const response = await fetch(`${REST_API_ACCOUNT_URL}/${accountId}/avatar`, {
    method: "POST",
    body: formData,
  });

  if (!response.ok) {
    const errorText = await response.text(); // Read text response for debugging
    throw new Error(`Failed to update avatar: ${errorText}`);
  }

  try {
    return await response.json(); // Attempt to parse JSON if available
  } catch (error) {
    console.warn("Response is not in JSON format. Returning raw response.");
    return response; // Return raw response if not JSON
  }
};
