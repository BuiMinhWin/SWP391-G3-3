package com.example.demo.controller;

import com.example.demo.dto.request.AccountDTO;
import com.example.demo.service.iml.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/loginGG")
public class JwtController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccessToken")
    public String getAccessToken(HttpSession session) {
        // Lấy access token từ session hoặc từ Swagger UI

        String accessToken = (String) session.getAttribute("swaggerAccessToken");
        session.setAttribute("swaggerAccessToken", accessToken);
        System.out.println("Access Token stored in session: " + session.getAttribute("swaggerAccessToken"));
        if (accessToken != null) {
            return "Access Token: " + accessToken;
        } else {
            return "Access Token not found. Please login again.";
        }
    }

    @GetMapping("/loginGG")
    public ResponseEntity<Map<String, Object>> currentUser(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        if (oAuth2AuthenticationToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token error", "message", "No OAuth2 token found"));
        }

        // Lấy thông tin người dùng từ token
        Map<String, Object> userAttributes = oAuth2AuthenticationToken.getPrincipal().getAttributes();
        System.out.println(toPerson(userAttributes).getEmail());
        System.out.println(toPerson(userAttributes).getUserName());
        System.out.println(toPerson(userAttributes).getAvatar());

        return ResponseEntity.ok(userAttributes);
    }

    public AccountDTO toPerson(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setEmail((String) map.get("email"));
        accountDTO.setUserName((String) map.get("name"));
        accountDTO.setAvatar((String) map.get("picture"));
        return accountDTO;
    }

    public ResponseEntity<AccountDTO> toPerson2(Map<String, Object> map) {
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
