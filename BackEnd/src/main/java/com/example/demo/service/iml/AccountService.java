package com.example.demo.service.iml;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.IdGenerator;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.mapper.AccountMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.util.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AccountService {


    @Autowired
    private AccountRepository accountRepository;

    public AccountDTO createAccount(AccountDTO accountDTO) {
        if (accountRepository.existsByEmail(accountDTO.getEmail())) {
            throw new DuplicateEmailException("This email address '" + accountDTO.getEmail() + "' is already in use.");
        }

        if (accountRepository.existsByPhone(accountDTO.getPhone())) {
            throw new DuplicateEmailException("This phone number '" + accountDTO.getPhone() + "' is already in use.");
        }

        if (accountRepository.existsByUserName(accountDTO.getUserName())) {
            throw new DuplicateEmailException("This user name '" + accountDTO.getUserName() + "' is already in use.");
        }

        Account account = AccountMapper.mapToAccount(accountDTO);
        account.setAccountId(IdGenerator.generateId());
        if (accountDTO.getRoleId() == null || accountDTO.getRoleId().isEmpty()) {
            account.setRoleId("Customer");
        }
        if (accountDTO.getCreateAt() == null) {
            account.setCreateAt(LocalDateTime.now());
        }

        if (accountDTO.getAvatar() == null) {
            try {
                account.setAvatar(loadDefaultAvatar());
            } catch (IOException e) {
                throw new RuntimeException("Failed to load default avatar.", e);
            }
        }

        account.setStatus(1);

        Account savedAccount = accountRepository.save(account);
        return AccountMapper.maptoAccountDTO(savedAccount);
    }

    private byte[] loadDefaultAvatar() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/Avatar.jpg");
        try (InputStream inputStream = resource.getInputStream()) {
            return ImageUtils.compressImage(inputStream.readAllBytes());
        }
    }


    public AccountDTO getAccountById(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));

        AccountDTO accountDTO = AccountMapper.maptoAccountDTO(account);

        if (account.getAvatar() != null) {
            accountDTO.setAvatar(ImageUtils.decompressImage(account.getAvatar()));
        }

        return accountDTO;
    }


    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::maptoAccountDTO)
                .collect(Collectors.toList());
    }

    public AccountDTO updateAccount(String accountId, AccountDTO updatedAccountDTO) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));

        if (updatedAccountDTO.getFirstName() != null) {
            account.setFirstName(updatedAccountDTO.getFirstName());
        }
        if (updatedAccountDTO.getLastName() != null) {
            account.setLastName(updatedAccountDTO.getLastName());
        }
        if (updatedAccountDTO.getEmail() != null) {
            if (!account.getEmail().equals(updatedAccountDTO.getEmail()) &&
                    accountRepository.existsByEmail(updatedAccountDTO.getEmail())) {
                throw new DuplicateEmailException("This email address '" + updatedAccountDTO.getEmail() + "' is already in use.");
            }
            account.setEmail(updatedAccountDTO.getEmail());
        }

        if (updatedAccountDTO.getPhone() != null) {
            if (!account.getPhone().equals(updatedAccountDTO.getPhone()) &&
                    accountRepository.existsByPhone(updatedAccountDTO.getPhone())) {
                throw new DuplicateEmailException("This phone number '" + updatedAccountDTO.getPhone() + "' is already in use.");
            }
            account.setPhone(updatedAccountDTO.getPhone());
        }

        if (updatedAccountDTO.getUserName() != null) {
            account.setUserName(updatedAccountDTO.getUserName());
        }
        if (updatedAccountDTO.getPassword() != null) {
            account.setPassword(updatedAccountDTO.getPassword());
        }

        Account updatedAccount = accountRepository.save(account);
        return AccountMapper.maptoAccountDTO(updatedAccount);
    }

    public void deactivateAccount(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));
        account.setStatus(0);
        accountRepository.save(account);
    }

    public AccountDTO createAccountGG(AccountDTO accountDTO) {
        Account existingAccount = accountRepository.findByEmail(accountDTO.getEmail());
        if (existingAccount != null) {
            return AccountMapper.maptoAccountDTO(existingAccount);
        }

        Account account = AccountMapper.mapToAccount(accountDTO);
        account.setAccountId(IdGenerator.generateId());

        account.setUserName(accountDTO.getFirstName() + accountDTO.getLastName());

        if (accountDTO.getCreateAt() == null) {
            account.setCreateAt(LocalDateTime.now());
        }

        if (accountDTO.getAvatar() == null) {
            try {
                account.setAvatar(loadDefaultAvatar());
            } catch (IOException e) {
                throw new RuntimeException("Failed to load default avatar.", e);
            }
        }

        account.setStatus(1);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.maptoAccountDTO(savedAccount);
    }



    public String updateAvatar(MultipartFile file, String accountId) throws IOException {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));

        account.setAvatar(ImageUtils.compressImage(file.getBytes()));

        accountRepository.save(account);

        return "Avatar uploaded successfully.";
    }

    public byte[] getAvatar(String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account does not exist with id: " + accountId));

        return ImageUtils.decompressImage(account.getAvatar());
    }

}
