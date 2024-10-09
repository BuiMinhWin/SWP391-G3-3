package com.example.demo.config;

import com.example.demo.service.iml.AccountService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SpringSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccountService accountService) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/api/accounts/login", "/api/accounts", "/api/accounts/**", "/api/accounts/register"
                                , "/api/orders/create", "/api/orders/cancel/**", "/api/orders/update/**", "/api/orders", "/api/orders/**"
                                , "/api/documents/create", "/api/documents/**", "/api/documents", "/api/documents/order/**"
                                , "/api/feedbacks/create", "/api/ordersDetail/create", "/api/loginGG/user-info"
                                , "/error", "/swagger-ui/**", "/v3/api-docs/**"
                                , "/swagger-resources/**", "/webjars/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .loginProcessingUrl("/doLogin")
                        .failureUrl("/login?error=true")
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/").permitAll()
                )
                .csrf(csrf -> csrf.disable())  // Vô hiệu hóa CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));  // Cấu hình CORS
  // Cấu hình xác thực JWT từ Google

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://koideliverysystem.id.vn", "http://koideliverysystem.id.vn:8080/api/accounts/google-login"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
