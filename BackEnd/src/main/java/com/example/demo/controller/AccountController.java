package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.service.iml.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("{accountId}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable("accountId") String accountId) {
        AccountDTO accountDTO = accountService.getAccountById(accountId);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/update/{accountId}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable("accountId") String accountId, @RequestBody AccountDTO updatedAccountDTO) {
        AccountDTO accountDTO = accountService.updateAccount(accountId, updatedAccountDTO);
        return ResponseEntity.ok(accountDTO);
    }


    @PatchMapping("/deActive/{accountId}")
    public ResponseEntity<String> deActivateAccount(@PathVariable String accountId) {
        accountService.deActivateAccount(accountId);
        return ResponseEntity.ok("Account status set to inactive (soft delete) successfully.");
    }

    @PatchMapping("/active/{accountId}")
    public ResponseEntity<String> activateAccount(@PathVariable String accountId) {
        accountService.activateAccount(accountId);
        return ResponseEntity.ok("Account status set to active successfully.");
    }


    @PostMapping(value = "/{accountId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(
            @Parameter(description = "File to upload", required = true)
            @RequestPart("avatar") MultipartFile file,
            @PathVariable("accountId") String accountId) throws IOException {

        String uploadAvatar = accountService.updateAvatar(file, accountId);
        return ResponseEntity.status(HttpStatus.OK).body(uploadAvatar);
    }

    @GetMapping("/{accountId}/avatar")
    public ResponseEntity<ByteArrayResource> getAvatar(@PathVariable String accountId) {
        byte[] imageData = accountService.getAvatar(accountId);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new ByteArrayResource(imageData));
    }

}
