package com.example.demo.service.iml;

import com.example.demo.Login.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.dto.request.AccountCreation;
import com.example.demo.dto.request.AccountUpdate;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdGenerator;
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

    public LoginMessage loginUser(LoginDTO loginDTO) {
        Account account = accountRepository.findByUserName(loginDTO.getUserName());
        if (account == null) {
            account = accountRepository.findByEmail(loginDTO.getEmail());
        }
        if (account != null) {
            String inputPassword = loginDTO.getPassword();
            String encodedPassword = account.getPassword();
            if (inputPassword.equals(encodedPassword)) {
                Optional<Account> accountOptional = accountRepository.findOneByUserNameAndPassword(account.getUserName(), encodedPassword);
                if (accountOptional.isPresent()) {
                    return new LoginMessage("Login Success", true, account.getRoleId());
                } else {
                    return new LoginMessage("Login Failed", false, account.getRoleId());
                }
            } else {
                return new LoginMessage("Password Not Match", false, account.getRoleId());
            }
        } else {
            return new LoginMessage("User Name or Email not exists", false, account.getRoleId());
        }
    }

}
