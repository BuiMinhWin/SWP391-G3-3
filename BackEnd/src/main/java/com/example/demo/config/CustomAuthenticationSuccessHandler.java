package com.example.demo.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component()
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = null;

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_MANAGER")) {
                redirectUrl = "/manager/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_SALES")) {
                redirectUrl = "/sales/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_DELIVERY")) {
                redirectUrl = "/delivery/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                redirectUrl = "/customer/dashboard";
                break;
            }
        }

        // Kiểm tra URL redirect không null
        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        // Chuyển hướng
        response.sendRedirect(redirectUrl);
    }
}

