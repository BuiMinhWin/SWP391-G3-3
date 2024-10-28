package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/Google")
public class GoogleController {

    @Autowired
    private AccountService accountService;

    @CrossOrigin(origins = "https://koi-delivery-system.vercel.app")
    @PostMapping("/loginGG")
    public ResponseEntity<AccountDTO> createAccountGG(@RequestBody AccountDTO accountDTO) {
        AccountDTO savedAccount = accountService.createAccountGG(accountDTO);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }
}
