package com.example.demo.config;

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
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String redirectUrl = null;

        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_MANAGER")) {
                redirectUrl = "/Manager/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_SALES")) {
                redirectUrl = "/Sales/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_DELIVERY")) {
                redirectUrl = "Delivery/dashboard";
                break;
            } else if (authority.getAuthority().equals("ROLE_CUSTOMER")) {
                redirectUrl = "Customer/dashboard";
                break;
            }
        }

        if (redirectUrl == null) {
            redirectUrl = "/";
        }

        response.sendRedirect(redirectUrl);
    }
}

