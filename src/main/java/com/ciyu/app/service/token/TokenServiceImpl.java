package com.ciyu.app.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ciyu.app.dto.token.TokenDto;
import com.ciyu.app.dto.token.VerificationData;
import com.ciyu.app.dto.token.VerificationResult;
import com.ciyu.app.exception.DecryptFailedException;
import com.ciyu.app.exception.UnauthorizedException;
import com.ciyu.app.exception.VerificationFailedException;
import com.ciyu.app.pojo.User;
import com.ciyu.app.repository.UserRepo;
import com.ciyu.app.service.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Service @Slf4j @RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{
    private final UserService userService;
    public VerificationResult requestVerification(String loginToken) {
        RestTemplate template = new RestTemplate();
        String url  = "https://api.verification.jpush.cn/v1/web/loginTokenVerify?loginToken=" + loginToken;
        HttpHeaders headers = new HttpHeaders();
        // 可在极光开发者服务的 Web 控制台[应用设置]-[应用信息]中查看
        String appKey = "[your appkey]";
        String masterSecret = "[your master secret]";
        // 对应 curl -u 的内容
        String raw = String.format("%s:%s", appKey, masterSecret);
        String base64 = Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
        // Basic 认证模式
        headers.set("Authorization", String.format("Basic %s", base64));
        headers.set("Content-Type", "application/json");
        VerificationData data = new VerificationData(loginToken);
        HttpEntity<VerificationData> request = new HttpEntity<>(data, headers);
        HttpEntity<VerificationResult> response = template.exchange(url, HttpMethod.POST, request, VerificationResult.class);
        VerificationResult verification = response.getBody();
        log.info("Verification result is: {}", verification);
        if (verification == null || verification.getCode() != 8000) {
            if (verification != null) throw new VerificationFailedException(verification.getContent());
            throw new VerificationFailedException();
        }
        return verification;
    }


    public String decodePhone(String encodedPhone) {
        String key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANdzfFT1se8mZN1m" +
                "+ZD4G9WK6D6Wy6OMP2/znCURf6WwjE/Wq8X7P+Amf7Tbdnh4oDCGL4BFKqFh1dEr" +
                "VibuUhWds/1hlhxelp6ljbTJDKLRyxIxPciEdnKeSAmxNTulygDeJsC6AY1GRvtm" +
                "5ibftr8DS4W3EuVVGxZbM4pD8P0jAgMBAAECgYBSs/UFE+9CkuVjC7U/Dc55cUHn" +
                "TLFMzXhIfCXzIL10F8o0l34GmISTgZPCFABtDDjJh6jSPzq3Cjwud8kxVohRKTJd" +
                "nVzPcZkG/4iM8O0Wf+8qzJb/rzqGmJTHjiNMo7zHKZklHxasl602IU2QmH527cEU" +
                "9xSeBKnq0PGlvsfU0QJBAPqaQWNXXH7dyXeeFp1ORiqEiZqSlrNF+yNe7bmzY/SQ" +
                "ju/0hFiaJsuBBtVEwaCyM9bbxK+9gobhuL21TiN3i2sCQQDcF2qGro5kwmw3rdro" +
                "OZR1eiOk6DpfeCYfBEH3F2m9PPTxn1jHku/X1exxb7eCVP3V6LrzrXWEO/8UqR6+" +
                "fjspAkEA4QRtPiQNW6UN56g9qhXEjxze6bpCMsrdi48LajkXwVuj3PXzeBNCTmyW" +
                "lBzet8oLHhvLafmCIFbBqSYI+XO37QJAB1kQyWZSgrKrAkDfqhkiB7oILp1CNaXB" +
                "QA/YU/OIKDU8yERVpnQVZdNSjYb5TXSg9N9MPo5v+fY20e3DhQCZ4QJAIhZf+hT3" +
                "VMB5p0t4dmy6DwRKF5dUqfD7A+gyhdD/16jfPM3GWtK0ER7fgBfFQu9g2JQ3gKBh" +
                "JzhKCuYm0dvv4w==";
        try {
            return decrypt(encodedPhone, key);
        } catch (Exception e) {
            throw new DecryptFailedException();
        }
    }

    @Override
    public TokenDto signToken(String phone) {
        User user = userService.findUserByPhone(phone).orElseThrow();
        Algorithm algorithm = Algorithm.HMAC256("CiyuConquersAll");
        String accessToken = JWT.create()
                .withSubject(user.getPhone())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer("Ciyu")
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getPhone())
                .withExpiresAt(new Date(System.currentTimeMillis() + 180L * 24 * 60 * 60 * 1000))
                .withIssuer("Ciyu")
                .sign(algorithm);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setAccessToken(accessToken);
        tokenDto.setRefreshToken(refreshToken);
        return tokenDto;
    }

    @Override
    public TokenDto refreshToken(String token) {
        log.info(token);
        try {
            Algorithm algorithm = Algorithm.HMAC256("CiyuConquersAll");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String phone = decodedJWT.getSubject();
            User user = userService.findUserByPhone(phone).orElseThrow();
            String accessToken = JWT.create()
                    .withSubject(user.getPhone())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                    .withIssuer("Ciyu")
                    .sign(algorithm);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setAccessToken(accessToken);
            tokenDto.setRefreshToken(token);
            return tokenDto;
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    @Override
    public String extractPhone(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("CiyuConquersAll");
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (Exception exception) {
            throw new UnauthorizedException();
        }
    }

    private String decrypt(String crypto, String key) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(key));
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] b = Base64.getDecoder().decode(crypto);
        return new String(cipher.doFinal(b));
    }
}
