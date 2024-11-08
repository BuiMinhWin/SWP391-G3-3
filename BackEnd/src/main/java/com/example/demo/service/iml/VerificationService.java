package com.example.demo.service.iml;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class VerificationService {

    @Autowired
    private AccountRepository accountRepository;

    private Map<String, VerificationData> verificationCodes = new HashMap<>();

    private static class VerificationData {
        String code;
        long timestamp;

        VerificationData(String code, long timestamp) {
            this.code = code;
            this.timestamp = timestamp;
        }
    }

    public void saveVerificationCode(String email, String code) {
        long currentTime = System.currentTimeMillis();
        verificationCodes.put(email, new VerificationData(code, currentTime));
    }

    public boolean verifyCode(String email, String code) {
        VerificationData storedData = verificationCodes.get(email);

        if (storedData == null) {
            return false;
        }

        boolean isCodeValid = storedData.code.equals(code);
        boolean isExpired = System.currentTimeMillis() - storedData.timestamp > TimeUnit.MINUTES.toMillis(5);

        return isCodeValid && !isExpired;
    }

    public ResponseEntity<String> resetPassword(String email, String code, String newPassword, String confirmPassword) {
        if (!verifyCode(email, code)) {
            return new ResponseEntity<>("Invalid or expired verification code.", HttpStatus.BAD_REQUEST);
        }

        if (!newPassword.equals(confirmPassword)) {
            return new ResponseEntity<>("Passwords do not match. Please enter the same password in both fields.", HttpStatus.BAD_REQUEST);
        }

        if (newPassword.length() < 8) {
            return new ResponseEntity<>("Password must be at least 8 characters long.", HttpStatus.BAD_REQUEST);
        }
        if (!newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*[a-z].*") || !newPassword.matches(".*\\d.*")) {
            return new ResponseEntity<>("Password must contain at least one uppercase letter, one lowercase letter, and one number.", HttpStatus.BAD_REQUEST);
        }

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            return new ResponseEntity<>("Account not found with the provided email.", HttpStatus.NOT_FOUND);
        }

        if (account.getStatus() == 0) {
            return new ResponseEntity<>("Account is inactive and cannot reset the password.", HttpStatus.FORBIDDEN);
        }

        account.setPassword(newPassword);
        accountRepository.save(account);

        // Invalidate the verification code after successful password reset
        verificationCodes.remove(email);

        return new ResponseEntity<>("Your password has been successfully reset.", HttpStatus.OK);
    }

}
