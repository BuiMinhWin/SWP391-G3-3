package com.example.demo.service.iml;

import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VerificationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Map<String, String> verificationCodes = new HashMap<>();

    public void saveVerificationCode(String email, String code) {
        verificationCodes.put(email, code);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = verificationCodes.get(email);
        return storedCode != null && storedCode.equals(code);
    }

    public String resetPassword(String email, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match. Please enter the same password in both fields.";
        }

        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            return "Account not found with the provided email.";
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        account.setPassword(encodedPassword);

        accountRepository.save(account);

        return "Your password has been successfully reset.";
    }
}
