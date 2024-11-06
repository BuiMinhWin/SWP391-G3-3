package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {

    private String feedbackId;
    private String orderId;
    private int rating;
    private String comment;
    private FeedbackDTO responses;
    private LocalDateTime createdAt;
    private String accountId;
}
