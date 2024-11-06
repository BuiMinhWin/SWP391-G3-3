package com.example.demo.mapper;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

public class FeedbackMapper {

    public static FeedbackDTO maptoFeedbackDTO(Feedback feedback) {
        FeedbackDTO feedbackDTO = new FeedbackDTO();
        feedbackDTO.setFeedbackId(feedback.getFeedbackId());
        feedbackDTO.setOrderId(feedback.getOrder().getOrderId());
        feedbackDTO.setRating(feedback.getRating());
        feedbackDTO.setComment(feedback.getComment());
        feedbackDTO.setCreatedAt(feedback.getCreatedAt());
        feedbackDTO.setAccountId(feedback.getAccount().getAccountId());

        if (feedback.getResponse() != null) {
            feedbackDTO.setResponses(maptoFeedbackDTO(feedback.getResponse()));
        } else {
            feedbackDTO.setResponses(null);
        }

        return feedbackDTO;
    }

    public static Feedback mapToFeedback(FeedbackDTO feedbackDTO, Order order, Account account) {
        Feedback feedback = new Feedback();
        feedback.setFeedbackId(feedbackDTO.getFeedbackId());
        feedback.setOrder(order);
        feedback.setAccount(account);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRating(feedbackDTO.getRating());
        feedback.setCreatedAt(feedbackDTO.getCreatedAt());

        if (feedbackDTO.getResponses() != null) {
            Feedback response = mapToFeedback(feedbackDTO.getResponses(), order, account);
            response.setParentFeedback(feedback);
            feedback.setResponse(response);
        } else {
            feedback.setResponse(null);
        }

        return feedback;
    }


}
