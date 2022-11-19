package com.ciyu.app.dto.user;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class RegisterDto {
    @Pattern(message = "不是正确的手机号格式", regexp = "^[1]\\d{10}$")
    private String phone;
    private String password;
}
