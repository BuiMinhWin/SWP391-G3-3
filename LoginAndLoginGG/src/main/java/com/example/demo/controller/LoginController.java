package com.example.demo.controller;

import com.example.demo.Login.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private AccountService accountService;


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, Model model) {
        LoginMessage loginMessage = accountService.loginUser(loginDTO);

        if (loginMessage.getStatus()) {
            return "loginSuccess";
        } else {
            model.addAttribute("errorMessage", loginMessage.getMessage());
            return "login";
        }
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess() {
        return "loginSuccess";
    }
}
