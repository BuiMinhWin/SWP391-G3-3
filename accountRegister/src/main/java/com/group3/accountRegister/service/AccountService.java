package com.group3.accountRegister.service;

import com.group3.accountRegister.dto.request.AccountCreation;
import com.group3.accountRegister.dto.request.AccountUpdate;
import com.group3.accountRegister.entity.Account;
import com.group3.accountRegister.entity.IdGenerator;
import com.group3.accountRegister.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(AccountCreation request){
        Account account = new Account();

        account.setAccountId(IdGenerator.generateCustomUserId());
        account.setRoleId(request.getRoleId());
        account.setPassword(request.getPassword());
        account.setUserName(request.getUserName());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());
        account.setAvatar(request.getAvatar());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());
        account.setCreateAt(request.getCreateAt());

        return accountRepository.save(account);
    }

    public Account updateAccount(String userId, AccountUpdate request){
        Account account = getAccount(userId);

        account.setPassword(request.getPassword());
        account.setUserName(request.getUserName());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());
        account.setAvatar(request.getAvatar());
        account.setFirstName(request.getFirstName());
        account.setLastName(request.getLastName());

        return accountRepository.save(account);
    }

    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    public Account getAccount(String accountId){
        return accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));

    }

    public void deleteAccount(String accountId){
        accountRepository.deleteById(accountId);
    }
}
