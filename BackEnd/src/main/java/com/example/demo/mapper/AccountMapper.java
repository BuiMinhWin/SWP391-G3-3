package com.example.demo.mapper;


import com.example.demo.dto.request.AccountDTO;
import com.example.demo.entity.Account;

public class AccountMapper {

    public static AccountDTO maptoAccountDTO(Account account) {
        return new AccountDTO(
                account.getAccountId(),
                account.getFirstName(),
                account.getLastName(),
                account.getUserName(),
                account.getPassword(),
                account.getEmail(),
                account.getPhone(),
                account.getRoleId(),
                account.getAvatar(),
                account.getStatus(),
                account.getCreateAt()

        );
    }

    public static Account mapToAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountId(accountDTO.getAccountId());
        account.setFirstName(accountDTO.getFirstName());
        account.setLastName(accountDTO.getLastName());
        account.setUserName(accountDTO.getUserName());
        account.setPassword(accountDTO.getPassword());
        account.setEmail(accountDTO.getEmail());
        account.setPhone(accountDTO.getPhone());
        account.setRoleId(accountDTO.getRoleId());
        account.setStatus(accountDTO.getStatus());
        account.setAvatar(accountDTO.getAvatar());
        account.setCreateAt(accountDTO.getCreateAt());
        return account;
    }
}
