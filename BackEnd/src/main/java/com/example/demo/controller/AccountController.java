package com.example.demo.controller;



import com.example.demo.dto.request.AccountDTO;
import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO accountDTO) {
        AccountDTO savedAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }

    // Build Get Account by ID REST API
    @GetMapping("{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("accountId") String accountId) {
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountDTO);
    }

    // Build Get All Accounts REST API
    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Build Update Account REST API
    @PutMapping("{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable("accountId") String accountId, @RequestBody AccountDTO updatedAccountDTO) {
        AccountDTO accountDTO = accountService.updateAccount(accountId, updatedAccountDTO);
        return ResponseEntity.ok(accountDTO);
    }

    // Build Delete Account REST API
    @DeleteMapping("{accountId}")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountId") String accountId) {
        accountService.deleteAccount(accountId);
        return ResponseEntity.ok("Account deleted successfully");
    }

}
