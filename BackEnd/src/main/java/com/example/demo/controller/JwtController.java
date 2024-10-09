package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.service.iml.AccountService;
import com.example.demo.service.iml.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/loginGG")
public class JwtController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @GetMapping("/user-info")
    public ResponseEntity<?> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        if (oAuth2AuthenticationToken == null) {
            return new ResponseEntity<>("No user authenticated", HttpStatus.UNAUTHORIZED);
        }

        // Lấy thông tin từ OAuth2 token
        Map<String, Object> userAttributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();

        // Thay vì sử dụng "user-info", chúng ta lấy email từ token và tìm tài khoản bằng email
        String email = (String) userAttributes.get("email");

        // Kiểm tra và lấy account bằng email
        AccountDTO accountDTO = accountService.findAccountByEmail(email);
        if (accountDTO == null) {
            return new ResponseEntity<>("Account does not exist with email: " + email, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }


    // Chuyển đổi thông tin người dùng từ OAuth2 thành AccountDTO và lưu vào DB
    public ResponseEntity<AccountDTO> toPerson(Map<String, Object> map) {
        if (map == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail((String) map.get("email"));  // Lấy email từ thông tin OAuth2
        accountDTO.setUserName((String) map.get("name")); // Lấy tên người dùng
        accountDTO.setAvatar((String) map.get("picture")); // Lấy ảnh đại diện (nếu có)

        // Kiểm tra xem email này đã tồn tại trong DB hay chưa (nếu đã tồn tại thì không thêm lại)
        AccountDTO existingAccount = accountService.findAccountByEmail(accountDTO.getEmail());
        if (existingAccount != null) {
            // Nếu người dùng đã tồn tại, trả về thông tin người dùng mà không cần thêm mới
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        }

        // Lưu thông tin người dùng mới vào cơ sở dữ liệu
        AccountDTO savedAccount = accountService.createAccount(accountDTO);
        return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
    }
}
