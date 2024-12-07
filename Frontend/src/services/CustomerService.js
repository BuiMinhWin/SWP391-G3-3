import axios from "axios";

const REST_API_ORDER_URL = "http://koideliverysystem.id.vn:8080/api/orders";
const REST_API_ORDER_DETAIL_URL =
  "http://koideliverysystem.id.vn:8080/api/ordersDetail";
const REST_API_DOCUMENT_URL =
  "http://koideliverysystem.id.vn:8080/api/documents";
const REST_API_ACCOUNT_URL = "http://koideliverysystem.id.vn:8080/api/accounts";
const REST_API_DELI_URL =
  "http://koideliverysystem.id.vn:8080/api/deliveryStatus";
const REST_API_SERVICES_URL =
  "http://koideliverysystem.id.vn:8080/api/services";

export const createOrder = async (orderData) => {
  try {
    const response = await axios.post(
      `${REST_API_ORDER_URL}/create`,
      orderData
    );
    return response.data;
  } catch (error) {
    console.error("Error creating order:", error.message);
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

export const uploadDocument = (orderDetailId, formData) => {
  return axios.post(
    `http://koideliverysystem.id.vn:8080/api/documents/${orderDetailId}`,
    formData,
    {
      headers: { "Content-Type": "multipart/form-data" },
    }
  );
};

export const updateDocument = async (orderDetailId, formData) => {
  const response = await fetch(
    `http://koideliverysystem.id.vn:8080/api/documents/update/${orderDetailId}`,
    {
      method: "PUT",
      body: formData,
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to reupload document: ${errorText}`);
  }

  try {
    return await response.json();
  } catch (error) {
    console.warn("Response is not in JSON format. Returning raw response.");
    return response;
  }
};

export const updateOrderDetailStatus = async (orderDetailId, newStatus) => {
  const response = await fetch(
    `${REST_API_ORDER_DETAIL_URL}/update/${orderDetailId}`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ status: newStatus }),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to update order detail status: ${errorText}`);
  }

  try {
    return await response.json();
  } catch (error) {
    console.warn("Response is not in JSON format. Returning raw response.");
    return response;
  }
};
export const updateOrderStatus = async (orderId, newStatus) => {
  const response = await fetch(
    `${REST_API_ORDER_URL}/update/${orderId}`,
    {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ status: newStatus }),
    }
  );

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Failed to update order status: ${errorText}`);
  }

  try {
    return await response.json();
  } catch (error) {
    console.warn("Response is not in JSON format. Returning raw response.");
    return response;
  }
};

export const createOrderDetail = async (orderDetailData) => {
  try {
    const response = await axios.post(
      `${REST_API_ORDER_DETAIL_URL}/create`,
      orderDetailData,
      {
        headers: {
          "Content-Type": "application/json",
          // Add any other headers here, e.g., authorization if needed
        },
      }
    );
    console.log("Order detail created successfully:", response.data);
    return response.data;
  } catch (error) {
    console.error(
      "Error creating order detail:",
      error.response || error.message
    );
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
    throw new Error("Failed to cancel order");
  }

  // Only try to parse JSON if there is content
  const data = response.status !== 204 ? await response.json() : {};
  return data;
};

// export const getOrderPDF = async (orderIdDetail) => {
//   const response = await fetch(
//     `${REST_API_DOCUMENT_URL}/download/order/${orderIdDetail}`
//   );
//   if (!response.ok) {
//     throw new Error("Failed to fetch PDF");
//   }
//   return response.blob();
// };

export const getOrderPDF = async (orderDetailId) => {
  try {
    const response = await fetch(
      `${REST_API_DOCUMENT_URL}/download/order/${orderDetailId}`
    );

    if (!response.ok) {
      throw new Error("Failed to fetch PDF");
    }

    return await response.blob(); // Return blob for use as PDF in the UI
  } catch (error) {
    console.error("Error fetching PDF:", error);
    throw error; // Re-throw error for handling in the calling function
  }
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

export const updatePaymentStatus = async (orderId) => {
  try {
    console.log(`Sending PATCH request for order ID: ${orderId}`); // Debug log
    console.log(
      "Sending PATCH request with body:",
      JSON.stringify({ paymentStatus: 1 })
    );

    const response = await fetch(`${REST_API_ORDER_URL}/update/${orderId}`, {
      method: "PATCH",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ paymentStatus: 1 }),
    });

    if (!response.ok) {
      console.error(
        "PATCH request failed:",
        response.status,
        response.statusText
      );
      const errorData = await response.json();
      console.error("Error response data:", errorData);
      throw new Error("Failed to update payment status.");
    }

    const data = await response.json();
    console.log("Payment status successfully updated:", data);
    return data;
  } catch (error) {
    console.error("An error occurred in updatePaymentStatus:", error);
    throw error;
  }
};

export const getDeliveryStatusByOrderId = async (orderId) => {
  try {
    const response = await axios.get(
      `${REST_API_DELI_URL}/getAllDeliveryStatusByOrderId/${orderId}`
    );
    return response.data;
  } catch (error) {
    console.error("Error fetching delivery status:", error);
    throw error;
  }
};

export const fetchServices = async () => {
  try {
    const response = await fetch(`${REST_API_SERVICES_URL}`);
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    const servicesData = await response.json();
    console.log("Fetched Services Data:", servicesData);
    return servicesData;
  } catch (error) {
    console.error("Error fetching services:", error);
    return [];
  }
};
