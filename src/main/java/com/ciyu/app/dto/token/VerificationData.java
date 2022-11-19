package com.ciyu.app.dto.token;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class VerificationData {
    private String loginToken;
}