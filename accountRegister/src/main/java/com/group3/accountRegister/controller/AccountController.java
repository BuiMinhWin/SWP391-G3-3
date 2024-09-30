package com.group3.accountRegister.controller;

import com.group3.accountRegister.dto.request.AccountCreation;
import com.group3.accountRegister.dto.request.AccountUpdate;
import com.group3.accountRegister.entity.Account;
import com.group3.accountRegister.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping
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
