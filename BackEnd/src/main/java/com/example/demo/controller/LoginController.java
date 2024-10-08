package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.service.iml.AccountService;
import com.example.demo.service.iml.GoogleTokenVerifierService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/accounts")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GoogleTokenVerifierService googleTokenVerifierService;

    @PostMapping("/login")
    public ResponseEntity<LoginMessage> login(@RequestBody LoginDTO loginDTO) {
        return accountService.loginUser(loginDTO);
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> loginWithGoogle(@RequestBody String token) {
        try {
            GoogleIdToken idToken = googleTokenVerifierService.verifyToken(token);
            GoogleIdToken.Payload payload = idToken.getPayload();

            String email = payload.getEmail();
            String userName = (String) payload.get("name");
            String avatar = (String) payload.get("picture");

            AccountDTO accountDTO = new AccountDTO();
            accountDTO.setEmail(email);
            accountDTO.setUserName(userName);
            accountDTO.setAvatar(avatar);

            AccountDTO createdAccount = accountService.createAccount(accountDTO);

            return ResponseEntity.ok(createdAccount);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
    }


}
