import axios from 'axios';

const API_BASE_URL = "http://koideliverysystem.id.vn:8080/api/feedbacks";

export const getAllFeedbackByOrderId = async (orderId) => {
    try {
        const response = await axios.get(`${API_BASE_URL}/getAllFeedbackByOrderId/${orderId}`);
        console.log("Full API Response:", response);
        return response.data; 
    } catch (error) {
        console.error("Error fetching feedbacks:", error);
        throw error;
    }
};

export const respondToFeedback = async (feedbackId, payload) => {
    try {
        const response = await axios.post(`${API_BASE_URL}/respond/${feedbackId}`, payload);
        return response.data;
    } catch (error) {
        console.error("Error responding to feedback:", error);
        throw error;
    }
};