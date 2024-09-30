package com.example.demo.controller;


import com.example.demo.dto.request.AccountCreationRequest;
import com.example.demo.dto.request.AccountUpdateRequest;
import com.example.demo.entity.Account;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    Account createUser(@RequestBody AccountCreationRequest request) {
        return accountService.createUser(request);
    }

    @GetMapping
    List<Account> getAccount() {
        return accountService.getAccount();
    }

    @GetMapping("/{accountId}")
    Account getAccount(@PathVariable("accountId") String accountId) {
        return accountService.getAccount(accountId);
    }

    @PutMapping("/{accountId}")
    Account updateAccount(@PathVariable String accountId, @RequestBody AccountUpdateRequest request) {
        return accountService.updateAccount(accountId, request);
    }

    @DeleteMapping("/{accountId}")
    String deleteAccount(@PathVariable String accountId) {
        accountService.deleteAccount(accountId);
        return "Account has been deleted";
    }

}
