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
        // Try to find account by userName
        Account account = accountRepository.findByUserName(loginDTO.getUserName());

        // If not found by userName, try email
        if (account == null && loginDTO.getEmail() != null) {
            account = accountRepository.findByEmail(loginDTO.getEmail());
        }

        // If still not found, try phone
        if (account == null && loginDTO.getPhone() != null) {
            account = accountRepository.findByPhone(loginDTO.getPhone());
        }

        // If account is still not found, return NOT_FOUND response
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new LoginMessage("User Name or Email or Phone not exists", false, null, null));
        }

        // Check if the account is inactive
        if (account.getStatus() == 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginMessage("Login Failed: Account is inactive.", false, null, null));
        }

        // Verify the password
        String inputPassword = loginDTO.getPassword();
        String encodedPassword = account.getPassword();

        if (inputPassword.equals(encodedPassword)) {
            // Successful login
            return ResponseEntity.ok(
                    new LoginMessage("Login Success", true, account.getRoleId(), account.getAccountId())
            );
        } else {
            // Password mismatch
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginMessage("Password Not Match", false, null, null));
        }
    }


}
