package com.ciyu.app.controller;

import com.ciyu.app.dto.user.RegisterDto;
import com.ciyu.app.exception.PhoneAlreadyExistsException;
import com.ciyu.app.pojo.Glossary;
import com.ciyu.app.pojo.User;
import com.ciyu.app.pojo.Word;
import com.ciyu.app.security.CurrentUser;
import com.ciyu.app.security.IgnoreSecurity;
import com.ciyu.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    @IgnoreSecurity
    public void register(@RequestBody RegisterDto registerDto) {
        if (userService.findUserByPhone(registerDto.getPhone()).isPresent())
            throw new PhoneAlreadyExistsException();
        userService.saveUser(new User().setPhone(registerDto.getPhone()).setPassword(registerDto.getPassword()));
    }
}
