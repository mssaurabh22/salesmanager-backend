package org.crm.salesmanager.auth.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.crm.salesmanager.auth.dto.AuthResponse;
import org.crm.salesmanager.auth.dto.LoginRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenResponse;
import org.crm.salesmanager.auth.entity.RefreshToken;
import org.crm.salesmanager.auth.security.JwtService;
import org.crm.salesmanager.auth.service.service.AuthService;
import org.crm.salesmanager.auth.service.service.RefreshTokenService;
import org.crm.salesmanager.user.dto.UserAuthDTO;
import org.crm.salesmanager.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    
    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        log.debug("Processing login request for email: {}", loginRequest.getEmail());
        
        UserAuthDTO user = userService.getUserForAuth(loginRequest.getEmail());
        
        if (!user.getActive()) {
            throw new RuntimeException("User account is inactive");
        }
        
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .build();
    }
    
    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        log.debug("Processing refresh token request");
        
        RefreshToken refreshToken = refreshTokenService.validateToken(refreshTokenRequest.getRefreshToken());
        UserAuthDTO user = userService.getUserForAuthById(refreshToken.getUserId());
        
        if (!user.getActive()) {
            throw new RuntimeException("User account is inactive");
        }
        
        String newAccessToken = jwtService.generateAccessToken(user);
        
        return RefreshTokenResponse.builder()
                .accessToken(newAccessToken)
                .tokenType("Bearer")
                .build();
    }
    
    @Override
    public void logout(String refreshToken) {
        log.debug("Processing logout request");
        refreshTokenService.revokeToken(refreshToken);
    }
}