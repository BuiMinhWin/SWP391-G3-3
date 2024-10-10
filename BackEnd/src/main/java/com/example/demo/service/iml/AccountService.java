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

    public AccountDTO findAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account != null) {
            return AccountMapper.maptoAccountDTO(account);
        }
        return null;
    }

    public void save(Account account) {
        accountRepository.save(account);
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

        // Update only the fields that are not null in updatedAccountDTO
        if (updatedAccountDTO.getFirstName() != null) {
            account.setFirstName(updatedAccountDTO.getFirstName());
        }
        if (updatedAccountDTO.getLastName() != null) {
            account.setLastName(updatedAccountDTO.getLastName());
        }
        if (updatedAccountDTO.getEmail() != null) {
            // Check for duplicate email only if email is being updated
            if (!account.getEmail().equals(updatedAccountDTO.getEmail()) &&
                    accountRepository.existsByEmail(updatedAccountDTO.getEmail())) {
                throw new DuplicateEmailException("This email address '" + updatedAccountDTO.getEmail() + "' is already in use.");
            }
            account.setEmail(updatedAccountDTO.getEmail());
        }
        if (updatedAccountDTO.getRoleId() != null) {
            account.setRoleId(updatedAccountDTO.getRoleId());
        }
        if (updatedAccountDTO.getUserName() != null) {
            account.setUserName(updatedAccountDTO.getUserName());
        }
        if (updatedAccountDTO.getPassword() != null) {
            account.setPassword(updatedAccountDTO.getPassword());
        }
        if (updatedAccountDTO.getAvatar() != null) {
            account.setAvatar(updatedAccountDTO.getAvatar());
        }
        // Add more fields as necessary

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

}
