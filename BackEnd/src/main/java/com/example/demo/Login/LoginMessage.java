package com.example.demo.Login;

public class LoginMessage {
    private String message;
    private Boolean status;
    private String roleId;
    private String accountId; // New field

    // Updated constructor to include accountId
    public LoginMessage(String message, Boolean status, String roleId, String accountId) {
        this.message = message;
        this.status = status;
        this.roleId = roleId;
        this.accountId = accountId; // Initialize the new field
    }

    // Additional constructor for backward compatibility if needed
    public LoginMessage(String message, Boolean status, String roleId) {
        this.message = message;
        this.status = status;
        this.roleId = roleId;
    }

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
    public String getRoleId() {
        return roleId;
    }
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    // Getter and Setter for accountId
    public String getAccountId() {
        return accountId;
    }
    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
