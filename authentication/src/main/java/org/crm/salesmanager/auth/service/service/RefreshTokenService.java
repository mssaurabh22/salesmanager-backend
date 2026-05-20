package org.crm.salesmanager.auth.service.service;


import org.crm.salesmanager.auth.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(Long userId);
    RefreshToken validateToken(String token);
    void revokeToken(String token);
    void revokeAllUserTokens(Long userId);
}