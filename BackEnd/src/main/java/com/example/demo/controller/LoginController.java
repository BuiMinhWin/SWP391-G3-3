package com.example.demo.controller;

import com.example.demo.dto.request.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class LoginController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<LoginMessage> login(@RequestBody LoginDTO loginDTO) {
        LoginMessage loginMessage = accountService.loginUser(loginDTO);

        if (loginMessage.getStatus()) {
            return new ResponseEntity<>(loginMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(loginMessage, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/google")
    public OAuth2User getUser(@AuthenticationPrincipal OAuth2User principal) {
        return principal;
    }

}
