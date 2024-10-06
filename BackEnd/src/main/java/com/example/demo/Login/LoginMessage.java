package com.example.demo.Login;

public class LoginMessage {
    String message;
    Boolean status;
    String roleId;

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }

    public LoginMessage(String message, Boolean status, String roleId) {
        this.message = message;
        this.status = status;
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
