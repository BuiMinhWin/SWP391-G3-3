package com.example.demo.controller;

import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth2")
public class OAuth2Controller {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.token.uri}")
    private String tokenUri;

    // Step 1: Redirect to Google's OAuth 2.0 authorization page
    @GetMapping("/authorize")
    public String authorize() {
        String authUrl = String.format(
                "https://accounts.google.com/o/oauth2/v2/auth" +
                        "?client_id=%s&redirect_uri=%s&response_type=code&scope=%s&access_type=offline",
                clientId, redirectUri, "https://www.googleapis.com/auth/drive.file");

        return "Redirect to: " + authUrl;
    }

    // Step 2: Handle the OAuth 2.0 callback and exchange code for access and refresh tokens
    @GetMapping("/callback")
    public Map<String, String> callback(@RequestParam("code") String code) throws IOException {
        OkHttpClient client = new OkHttpClient();

        // Prepare request body for token exchange
        RequestBody body = new FormBody.Builder()
                .add("code", code)
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("redirect_uri", redirectUri)
                .add("grant_type", "authorization_code")
                .build();

        Request request = new Request.Builder()
                .url(tokenUri)
                .post(body)
                .build();

        // Send request to Google's OAuth 2.0 token endpoint
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            // Parse the response JSON and return the access token and refresh token
            Map<String, String> result = new HashMap<>();
            String responseBody = response.body().string();
            System.out.println("Response: " + responseBody);
            // In production, you would parse the JSON properly here

            result.put("tokens", responseBody); // Simplified
            return result;
        }
    }

    @GetMapping("/get-access-token")
    public String getAccessToken(@RequestParam("refresh_token") String refreshToken) throws IOException {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("client_id", clientId)
                .add("client_secret", clientSecret)
                .add("refresh_token", refreshToken)
                .add("grant_type", "refresh_token")
                .build();

        Request request = new Request.Builder()
                .url(tokenUri)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            System.out.println("New Access Token: " + responseBody);

            return responseBody; // Return new access token
        }
    }
}