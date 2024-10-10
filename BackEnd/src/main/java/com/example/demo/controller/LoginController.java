package com.example.demo.controller;

import com.example.demo.dto.request.LoginDTO;
import com.example.demo.service.iml.AccountService;
import com.example.demo.service.iml.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = {"http://localhost:3000", "http://koideliverysystem.id.vn"}, allowCredentials = "true")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        return loginService.loginUser(loginDTO);
    }


}