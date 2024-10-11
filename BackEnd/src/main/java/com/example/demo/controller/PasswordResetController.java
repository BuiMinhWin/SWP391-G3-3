package com.example.demo.controller;

import com.example.demo.service.iml.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private MailService mailService;

    @PostMapping("/request-password-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        String resetLink = "https://yourdomain.com/reset-password?token=someUniqueToken";

        mailService.sendResetPasswordEmail(email, resetLink);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset email has been sent to " + email);

        return ResponseEntity.ok(response);
    }
}
