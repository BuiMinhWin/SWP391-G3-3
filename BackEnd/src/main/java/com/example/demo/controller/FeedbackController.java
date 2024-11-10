package com.example.demo.controller;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.entity.Feedback;
import com.example.demo.mapper.FeedbackMapper;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.service.iml.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @PostMapping("/create")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO savedFeedback = feedbackService.createFeedback(feedbackDTO);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    @GetMapping("/getAllFeedbackByOrderId/{orderId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByOrderId(@PathVariable String orderId) {
        List<FeedbackDTO> feedbackDTOs = feedbackService.getFeedbacksByOrderId(orderId);
        return ResponseEntity.ok(feedbackDTOs);
    }

    @PostMapping("/respond/{feedbackId}")
    public ResponseEntity<FeedbackDTO> respondToCustomerFeedback(
            @PathVariable String feedbackId,
            @RequestBody FeedbackDTO responseDTO) {
        FeedbackDTO responseFeedback = feedbackService.respondToCustomerFeedback(feedbackId, responseDTO);
        return ResponseEntity.ok(responseFeedback);
    }

    @DeleteMapping("/delete/{feedbackId}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable String feedbackId) {
        feedbackService.deleteFeedback(feedbackId);
        return ResponseEntity.noContent().build();
    }


}