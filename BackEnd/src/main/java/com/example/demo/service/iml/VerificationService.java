package com.example.demo.service.iml;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        verificationCodes.remove(email);

        return isCodeValid && !isExpired;
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match. Please enter the same password in both fields.";
        }

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            return "Account not found with the provided email.";
        }

        account.setPassword(newPassword);

        accountRepository.save(account);

        return "Your password has been successfully reset.";
    }
}
