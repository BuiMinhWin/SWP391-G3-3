// Replace with the actual base URL of your API for orders
const REST_API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/orders";

// Function to list all orders
export const listOrders = () => {
  return axios.get(REST_API_BASE_URL, {
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true, // If your API requires authentication cookies
  });
};

// Function to update order status
export const updateOrderStatus = (orderId, newStatus) => {
  return axios.put(
    `${REST_API_BASE_URL}/${orderId}/status`, 
    { status: newStatus },
    {
      headers: { 'Content-Type': 'application/json' },
      withCredentials: true,  // If your API requires authentication cookies
    }
  );
};