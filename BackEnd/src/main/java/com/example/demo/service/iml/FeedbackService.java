package com.example.demo.service.iml;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.IdGenerator;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDateTime;

public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO, OrderDTO orderDTO) {

        Feedback feedback = FeedbackMapper.mapToFeedback(feedbackDTO);
        feedback.setFeedbackId(IdGenerator.generateId());

        feedback.setOrderId(orderDTO.getOrderId());

        if (feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        } else {
            feedback.setRating(feedbackDTO.getRating());
        }

        if (feedbackDTO.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return FeedbackMapper.maptoFeedbackDTO(savedFeedback);
    }

}
