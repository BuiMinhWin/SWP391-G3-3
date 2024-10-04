package com.example.demo.controller;

import com.example.demo.dto.request.FeedbackDTO;
import com.example.demo.dto.request.OrderDTO;
import com.example.demo.service.iml.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO, @RequestBody String orderDTO) {
        try {
            FeedbackDTO createdFeedback = feedbackService.createFeedback(feedbackDTO, orderDTO);
            return new ResponseEntity<>(createdFeedback, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}