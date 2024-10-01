package com.example.demo.controller;



import com.example.demo.dto.request.AccountCreation;
import com.example.demo.dto.request.AccountUpdate;
import com.example.demo.entity.Account;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    Account createAccount(@RequestBody AccountCreation request){
        return accountService.createAccount(request);
    }

    @GetMapping
    List<Account> getAccounts(){
        return accountService.getAccounts();
    }

    @GetMapping("/{accountId}")
    Account getAccount(@PathVariable("accountId") String accountId){
        return accountService.getAccount(accountId);
    }

    @PutMapping("/{accountId}")
    Account updateAccount(@PathVariable String accountId, @RequestBody AccountUpdate request){
        return accountService.updateAccount(accountId, request);

    }

    @DeleteMapping("/{accountId}")
    String deleteAccount(@PathVariable String accountId){
        accountService.deleteAccount(accountId);
        return "Account has been deleted";
    }




}
