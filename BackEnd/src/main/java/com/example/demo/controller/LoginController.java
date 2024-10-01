package com.example.demo.controller;

import com.example.demo.Login.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.entity.Account;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
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

    @GetMapping("/manager/dashboard")
    public String managerDashboard(@AuthenticationPrincipal Account user, Model model) {
        model.addAttribute("username", user.getUserName());
        return "managerDashboard";  // Trả về managerDashboard.html khi đăng nhập thành công
    }
}
