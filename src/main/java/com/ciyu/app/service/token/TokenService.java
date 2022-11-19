package com.ciyu.app.service.token;

import com.ciyu.app.dto.token.TokenDto;
import com.ciyu.app.dto.token.VerificationResult;

public interface TokenService {
    VerificationResult requestVerification(String loginToken);
    String decodePhone(String encodedPhone);
    TokenDto signToken(String phone);
    TokenDto refreshToken(String token);
    String extractPhone(String token);
}
