package com.example.demo.Login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginMessage {
    private String message;
    private Boolean status;
    private String roleId;
    private String accountId;

}
