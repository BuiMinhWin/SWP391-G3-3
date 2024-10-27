package com.example.demo.service.iml;

import com.example.demo.Login.LoginMessage;
import com.example.demo.dto.request.LoginDTO;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@AllArgsConstructor
public class LoginService {

    @Autowired
    private AccountRepository accountRepository;

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
                    return ResponseEntity.ok(
                            new LoginMessage("Login Success", true, account.getRoleId(), account.getAccountId())
                    );
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body(new LoginMessage("Login Failed", false, account.getRoleId(), null));
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new LoginMessage("Password Not Match", false, account.getRoleId(), null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginMessage("User Name or Email not exists", false, null, null));
        }
    }
}