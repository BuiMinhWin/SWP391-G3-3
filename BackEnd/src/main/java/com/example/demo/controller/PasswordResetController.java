package com.example.demo.controller;

import com.example.demo.repository.AccountRepository;
import com.example.demo.service.iml.MailService;
import com.example.demo.service.iml.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String forgotPassword(@RequestParam String email) {
        String verificationCode = mailService.generateVerificationCode();
        mailService.sendVerificationCode(email, verificationCode);
        verificationService.saveVerificationCode(email, verificationCode);
        return "A verification code has been sent to your email.";
    }

    @PostMapping("/verify")
    public String verifyCode(@RequestParam String email, @RequestParam String code, @RequestParam String newPassword,@RequestParam String confirmPassword) {
        if (verificationService.verifyCode(email, code)) {
            verificationService.resetPassword(email, newPassword, confirmPassword);
            return "Your password has been successfully reset.";
        } else {
            return "Invalid verification code.";
        }
    }

}
