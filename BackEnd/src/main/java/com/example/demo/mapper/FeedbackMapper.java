package com.example.demo.mapper;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Feedback;

public class FeedbackMapper {

    public static FeedbackDTO maptoFeedbackDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getFeedbackId(),
                feedback.getOrderId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getCreatedAt(),
                feedback.getStatus()
        );
    }

    public static Feedback mapToFeedback(FeedbackDTO feedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackDTO.getFeedbackId());
        feedback.setOrderId(feedbackDTO.getOrderId());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setCreatedAt(feedbackDTO.getCreatedAt());
        feedback.setStatus(feedbackDTO.getStatus());
        return feedback;
    }
}
