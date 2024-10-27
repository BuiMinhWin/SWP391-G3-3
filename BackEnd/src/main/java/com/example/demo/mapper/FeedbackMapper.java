package com.example.demo.mapper;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;

public class FeedbackMapper {

    public static FeedbackDTO maptoFeedbackDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getFeedbackId(),
                feedback.getOrder().getOrderId(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getCreatedAt(),
                feedback.getAccount().getAccountId()
        );
    }

    public static Feedback mapToFeedback(FeedbackDTO feedbackDTO, Order order, Account account) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackDTO.getFeedbackId());
        feedback.setOrder(order);
        feedback.setRating(feedbackDTO.getRating());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setCreatedAt(feedbackDTO.getCreatedAt());
        feedback.setAccount(account);
        return feedback;
    }
}
