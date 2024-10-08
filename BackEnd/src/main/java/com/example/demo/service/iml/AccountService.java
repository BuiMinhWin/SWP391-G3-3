package com.example.demo.service.iml;

import com.example.demo.dto.request.LoginDTO;
import com.example.demo.Login.LoginMessage;
import com.example.demo.dto.request.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdGenerator;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;

@Service
@AllArgsConstructor
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
            throw new DuplicateEmailException("This email address '" + accountDTO.getEmail() + "' is already in use." );
        }

        Account account = AccountMapper.mapToAccount(accountDTO);
        account.setAccountId(IdGenerator.generateId());
        if (accountDTO.getRoleId() == null || accountDTO.getRoleId().isEmpty()) {
            account.setRoleId("Customer");
        }
        if (accountDTO.getCreateAt() == null) {
            account.setCreateAt(LocalDateTime.now());
        }
        if (accountDTO.getAvatar() == null || accountDTO.getAvatar().isEmpty()) {
            account.setAvatar("");
        }
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.maptoAccountDTO(savedAccount);
    }

    public AccountDTO getAccountById(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));
        return AccountMapper.maptoAccountDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::maptoAccountDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO updateAccount(String accountId, AccountDTO updatedAccountDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));

        account.setFirstName(updatedAccountDTO.getFirstName());
        account.setLastName(updatedAccountDTO.getLastName());
        account.setEmail(updatedAccountDTO.getEmail());
        account.setRoleId(updatedAccountDTO.getRoleId());
        account.setUserName(updatedAccountDTO.getUserName());
        account.setPassword(updatedAccountDTO.getPassword());

        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.maptoAccountDTO(updatedAccount);
    }

    public void deleteAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));
        accountRepository.deleteById(accountId);
    }

    public AccountDTO getAccountByUserName(String userName, String password) {
        Optional<Account> accountOptional = accountRepository.findOneByUserNameAndPassword(userName,password);
        return accountOptional.map(AccountMapper::maptoAccountDTO).orElse(null);
    }


    public ResponseEntity<LoginMessage> loginUser(LoginDTO loginDTO) {
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
                    return ResponseEntity.ok(new LoginMessage("Login Success", true, account.getRoleId()));
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new LoginMessage("Login Failed", false, account.getRoleId()));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginMessage("Password Not Match", false, account.getRoleId()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginMessage("User Name or Email not exists", false, null));
        }
    }



}
