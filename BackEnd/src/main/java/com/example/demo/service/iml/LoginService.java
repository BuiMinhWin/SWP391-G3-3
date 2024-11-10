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



@Service
@AllArgsConstructor
public class LoginService {

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<LoginMessage> loginUser(LoginDTO loginDTO) {
        Account account = accountRepository.findByUserName(loginDTO.getUserName());

        if (account == null && loginDTO.getEmail() != null) {
            account = accountRepository.findByEmail(loginDTO.getEmail());
        }

        if (account == null && loginDTO.getPhone() != null) {
            account = accountRepository.findByPhone(loginDTO.getPhone());
        }

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginMessage("User Name or Email or Phone not exists", false, null, null));
        }

        if (account.getStatus() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginMessage("Login Failed: Account is inactive.", false, null, null));
        }
        
        String inputPassword = loginDTO.getPassword();
        String encodedPassword = account.getPassword();

        if (inputPassword.equals(encodedPassword)) {
            return ResponseEntity.ok(
                    new LoginMessage("Login Success", true, account.getRoleId(), account.getAccountId())
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginMessage("Password Not Match", false, null, null));
        }
    }


}
