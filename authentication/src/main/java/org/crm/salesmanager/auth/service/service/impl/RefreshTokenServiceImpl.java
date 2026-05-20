package org.crm.salesmanager.auth.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.crm.salesmanager.auth.entity.RefreshToken;
import org.crm.salesmanager.auth.repository.RefreshTokenRepository;
import org.crm.salesmanager.auth.service.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Value("${jwt.refresh-token-expiry}")
    private long refreshTokenExpiration;
    
    @Override
    @Transactional
    public RefreshToken createRefreshToken(Long userId) {
        log.debug("Creating refresh token for user: {}", userId);
        
        // Revoke existing tokens for the user
        revokeAllUserTokens(userId);
        
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration))
                .revoked(false)
                .build();
        
        return refreshTokenRepository.save(refreshToken);
    }
    
    @Override
    public RefreshToken validateToken(String token) {
        log.debug("Validating refresh token: {}", token);
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        
        if (refreshToken.getRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }
        
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token has expired");
        }
        
        return refreshToken;
    }
    
    @Override
    @Transactional
    public void revokeToken(String token) {
        log.debug("Revoking refresh token: {}", token);
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
        
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
    
    @Override
    @Transactional
    public void revokeAllUserTokens(Long userId) {
        log.debug("Revoking all refresh tokens for user: {}", userId);
        refreshTokenRepository.deleteByUserId(userId);
    }
}