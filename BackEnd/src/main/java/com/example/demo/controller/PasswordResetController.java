package com.example.demo.controller;

import com.example.demo.repository.AccountRepository;
import com.example.demo.service.iml.MailService;
import com.example.demo.service.iml.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {

    @Autowired
    private MailService mailService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String verificationCode = mailService.generateVerificationCode();

        boolean isSent = mailService.sendVerificationCode(email, verificationCode);

        if (isSent) {
            verificationService.saveVerificationCode(email, verificationCode);
            return ResponseEntity.ok("A verification code has been sent to your email.");
        } else {
            return ResponseEntity.badRequest().body("Email not exits.");
        }
        }

    @PostMapping("/verify")
    public ResponseEntity<String> resetPassword(
            @RequestParam String email,
            @RequestParam String code,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword) {

        return verificationService.resetPassword(email, code, newPassword, confirmPassword);
    }
}
