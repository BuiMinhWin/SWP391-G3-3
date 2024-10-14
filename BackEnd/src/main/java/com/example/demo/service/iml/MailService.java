package com.example.demo.service.iml;

import com.example.demo.repository.AccountRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;



import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AccountRepository accountRepository;

    public String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Tạo mã 6 chữ số
        return String.valueOf(code);
    }

    @Async
    public boolean sendVerificationCode(String email, String verificationCode) {
        if (!accountRepository.existsByEmail(email)) {
            return false;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Password Reset Verification Code");

            String htmlContent = "<div style='text-align: center; font-family: Arial, sans-serif;'>"
                    + "<h2 style='font-size: 24px; color: #333;'>Your verification code is:</h2>"
                    + "<p style='font-size: 30px; font-weight: bold; color: #171B36;'>" + verificationCode + "</p>"
                    + "<h3>Please do not share this code with anyone. "
                    + "<h3>The verification code is valid for</h3>"
                    + "<span style='font-size: 24px; font-weight: bold; color: #171B36;'>5 minutes</span>.</h3>"
                    + "</div>";


            helper.setText(htmlContent, true); // Enable HTML content

            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
