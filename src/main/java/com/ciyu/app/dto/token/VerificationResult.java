package com.ciyu.app.dto.token;

import lombok.Data;

@Data
public class VerificationResult {
    private Long id;
    private Integer code;
    private String phone;
    private String content;
}