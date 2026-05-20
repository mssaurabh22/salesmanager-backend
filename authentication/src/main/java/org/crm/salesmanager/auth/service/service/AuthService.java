package org.crm.salesmanager.auth.service.service;

import org.crm.salesmanager.auth.dto.AuthResponse;
import org.crm.salesmanager.auth.dto.LoginRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenRequest;
import org.crm.salesmanager.auth.dto.RefreshTokenResponse;

public interface AuthService {
    AuthResponse login(LoginRequest loginRequest);
    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    void logout(String refreshToken);
}