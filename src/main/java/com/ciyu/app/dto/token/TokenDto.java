package com.ciyu.app.dto.token;

import lombok.Data;

@Data
public class TokenDto {
    private String accessToken;
    private String refreshToken;
}
