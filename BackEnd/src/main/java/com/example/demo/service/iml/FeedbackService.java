package com.example.demo.service.iml;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Feedback;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {

        Order order = orderRepository.findById(feedbackDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + feedbackDTO.getOrderId()));

        Account account = accountRepository.findById(feedbackDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + feedbackDTO.getAccountId()));

        Feedback feedback = FeedbackMapper.mapToFeedback(feedbackDTO, order, account);

        if (feedbackDTO.getRating() < 1 || feedbackDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        if (feedbackDTO.getComment() == null) {
            feedback.setComment("");
        }

        if (feedbackDTO.getCreatedAt() == null) {
            feedback.setCreatedAt(LocalDateTime.now());
        }

            feedback.setStatus(0);

        System.out.println("Creating Feedback with Order ID: " + feedback.getOrder().getOrderId());

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackMapper.maptoFeedbackDTO(savedFeedback);
    }

    public List<FeedbackDTO> getFeedbacksByOrderId(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        List<Feedback> feedbacks = feedbackRepository.findByOrder(order);
        return feedbacks.stream()
                .map(FeedbackMapper::maptoFeedbackDTO)
                .collect(Collectors.toList());
    }

    public FeedbackDTO respondToCustomerFeedback(String feedbackId, FeedbackDTO responseDTO) {
        Feedback customerFeedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + feedbackId));

        Account account = accountRepository.findById(responseDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + responseDTO.getAccountId()));

        Feedback responseFeedback = new Feedback();
        responseFeedback.setOrder(customerFeedback.getOrder());
        responseFeedback.setAccount(account);
        responseFeedback.setComment(responseDTO.getComment());
        responseFeedback.setRating(0);
        responseFeedback.setCreatedAt(LocalDateTime.now());

        responseFeedback.setParentFeedback(customerFeedback);

        Feedback savedResponseFeedback = feedbackRepository.save(responseFeedback);

        List<Feedback> responses = customerFeedback.getResponses();
        if (responses == null) {
            responses = new ArrayList<>();
        }
        responses.add(savedResponseFeedback);
        customerFeedback.setResponses(responses);

        feedbackRepository.save(customerFeedback);

        return FeedbackMapper.maptoFeedbackDTO(customerFeedback);
    }

    public void deleteFeedback(String feedbackId) {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with id " + feedbackId));

        feedbackRepository.delete(feedback);
    }

}
