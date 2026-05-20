package org.crm.salesmanager.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.crm.salesmanager.auth.dto.AuthResponse;
import org.crm.salesmanager.auth.dto.LoginRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenResponse;
import org.crm.salesmanager.auth.service.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "JWT authentication APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns access and refresh tokens")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("Login attempt for email: {}", loginRequest.getEmail());

        try {
            AuthResponse response = authService.login(loginRequest);
            log.info("Login successful for email: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for email: {}, error: {}", loginRequest.getEmail(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Issues a new access token using a valid refresh token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Refresh token request received");

        try {
            RefreshTokenResponse response = authService.refreshToken(refreshTokenRequest);
            log.info("Token refresh successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revokes a refresh token")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Logout request received");

        try {
            authService.logout(refreshTokenRequest.getRefreshToken());
            log.info("Logout successful");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            throw e;
        }
    }
}
