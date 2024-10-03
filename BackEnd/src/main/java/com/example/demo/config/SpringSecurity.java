package com.example.demo.config;

import com.example.demo.service.iml.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurity {

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccountService accountService) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/api/accounts/login", "/api/accounts", "/api/accounts/**", "/api/accounts/register","/api/orders/create", "/error", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                        .requestMatchers("/Manager/**").hasRole("MANAGER")
                        .requestMatchers("/Sales/**").hasRole("SALES")
                        .requestMatchers("/Delivery/**").hasRole("DELIVERY")
                        .requestMatchers("/Customer/**").hasRole("CUSTOMER")
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .failureUrl("/login?error=true")
                        .successHandler(new CustomAuthenticationSuccessHandler())
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }

}
