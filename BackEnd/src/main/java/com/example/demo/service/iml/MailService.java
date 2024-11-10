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
            helper.setSubject("Mã xác minh để đặt lại mật khẩu");

            String htmlContent = "<div style='text-align: center; font-family: Arial, sans-serif;'>"
                    + "<h2 style='font-size: 24px; color: #333;'>Mã xác minh của bạn là:</h2>"
                    + "<p style='font-size: 30px; font-weight: bold; color: #171B36;'>" + verificationCode + "</p>"
                    + "<h3>Vui lòng không chia sẻ mã xác minh này với bất kỳ ai.</h3>"
                    + "<h3>Mã xác minh này có giá trị trong</h3>"
                    + "<span style='font-size: 24px; font-weight: bold; color: #171B36;'>5 phút</span>.</h3>"
                    + "</div>";


            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

}
