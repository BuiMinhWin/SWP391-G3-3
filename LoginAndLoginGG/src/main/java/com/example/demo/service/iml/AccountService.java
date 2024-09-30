package com.example.demo.service.iml;

import com.example.demo.Login.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.dto.request.AccountCreationRequest;
import com.example.demo.dto.request.AccountUpdateRequest;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {


    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public Account createUser(AccountCreationRequest request) {
        Account account = new Account();

        account.setUserName(request.getUserName());
        account.setPassword(request.getPassword());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());
        return accountRepository.save(account);
    }

    public List<Account> getAccount() {
        return accountRepository.findAll();
    }

    public Account getAccount(String id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Account updateAccount(String userId, AccountUpdateRequest request) {
        Account account = getAccount(userId);

        account.setPassword(request.getPassword());
        account.setPhone(request.getPhone());
        account.setEmail(request.getEmail());

        return accountRepository.save(account);
    }

    public void deleteAccount(String userId) {
        accountRepository.deleteById(userId);
    }

    public LoginMessage loginUser(LoginDTO loginDTO) {
        Account account = accountRepository.findByUserName(loginDTO.getUserName());
        if (account == null) {
            account = accountRepository.findByEmail(loginDTO.getEmail());
        }
        if (account != null) {
            String inputPassword = loginDTO.getPassword();
            String encodedPassword = account.getPassword();
            if (inputPassword.matches(encodedPassword)) {
                Optional<Account> account2 = accountRepository.findOneByUserNameAndPassword(account.getUserName(), encodedPassword);
                if (account2.isPresent()) {
                    return new LoginMessage("Login Success", true);
                } else {
                    return new LoginMessage("Login Failed", false);
                }
            } else {
                return new LoginMessage("Password Not Match", false);
            }
        } else {
            return new LoginMessage("User Name or Email not exits", false);
        }
    }
}
