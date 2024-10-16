package com.example.demo.controller;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.service.iml.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO savedFeedback = feedbackService.createFeedback(feedbackDTO);
        return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
    }

    @GetMapping("/getAllFeedbackByOrderId/{orderId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbacksByOrderId(@PathVariable String orderId) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksByOrderId(orderId);
        return new ResponseEntity<>(feedbacks, HttpStatus.OK);
    }

    @PostMapping("/respond/{feedbackId}")
    public ResponseEntity<FeedbackDTO> respondToFeedback(@PathVariable String feedbackId, @RequestBody FeedbackDTO responseDTO) {
        FeedbackDTO updatedFeedback = feedbackService.respondToCustomerFeedback(feedbackId, responseDTO);
        return ResponseEntity.ok(updatedFeedback);
    }



}