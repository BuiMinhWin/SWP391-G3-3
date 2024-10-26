package com.example.demo.response;

import com.example.demo.dto.request.AccountDTO;

public class AccountResponse {
    private AccountDTO accountDTO;
    private byte[] avatarImage;

    // Constructor, getters, and setters
    public AccountResponse(AccountDTO accountDTO, byte[] avatarImage) {
        this.accountDTO = accountDTO;
        this.avatarImage = avatarImage;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public byte[] getAvatarImage() {
        return avatarImage;
    }
}
