package com.example.demo.service.iml;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Feedback;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrderRepository orderRepository;

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO, String orderId) {

        Order order = orderRepository.findById(feedbackDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + feedbackDTO.getOrderId()));

        Feedback feedback = FeedbackMapper.mapToFeedback(feedbackDTO, order);

        if (feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        if (feedbackDTO.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }

        if (feedbackDTO.getStatus() == 0) {
            feedback.setStatus(1);
        }

        System.out.println("Creating Feedback with Order ID: " + feedback.getOrder().getOrderId());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackMapper.maptoFeedbackDTO(savedFeedback);
    }
}
