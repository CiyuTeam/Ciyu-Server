package com.ciyu.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ciyu.app.dto.token.RefreshTokenData;
import com.ciyu.app.dto.token.TokenDto;
import com.ciyu.app.dto.token.VerificationResult;
import com.ciyu.app.exception.BadRequestException;
import com.ciyu.app.exception.UnauthorizedException;
import com.ciyu.app.pojo.User;
import com.ciyu.app.security.IgnoreSecurity;
import com.ciyu.app.service.token.TokenService;
import com.ciyu.app.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/api/v1/tokens")
public class TokenController {
    private final UserService userService;
    private final TokenService tokenService;

    @GetMapping("/exchange")
    @IgnoreSecurity
    public TokenDto exchangeToken(@NotNull String loginToken) {
        log.info("Login token is: {}", loginToken);
        if (loginToken == null)
            throw new BadRequestException();
        // 获取极光校验结果
        VerificationResult verification = tokenService.requestVerification(loginToken);
        // 对返回的加密手机号进行解密
        String phone = tokenService.decodePhone(verification.getPhone());
        // 签发 token
        return tokenService.signToken(phone);
    }

//    @PostMapping("/refresh")
//    @IgnoreSecurity
//    public TokenDto refreshToken(HttpServletRequest request) {
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
//        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
//            throw new RuntimeException("Refresh token is missing");
//        // 通过 refreshToken 获取新的 accessToken
//        String refreshToken = authorizationHeader.substring("Bearer ".length());
//        return tokenService.refreshToken(refreshToken);
//    }

    @PostMapping("/refresh")
    @IgnoreSecurity
    public TokenDto refreshToken(@RequestBody RefreshTokenData refreshTokenData) {
        if (refreshTokenData == null || refreshTokenData.getRefreshToken() == null)
            throw new UnauthorizedException();
        return tokenService.refreshToken(refreshTokenData.getRefreshToken());
    }

    @GetMapping("/login")
    @IgnoreSecurity
    public TokenDto login(@NotBlank String phone, @NotBlank String password) {
        // 检查用户名和密码是否正确
        userService.checkPassword(phone, password);
        return tokenService.signToken(phone);
    }
}
