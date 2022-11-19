package com.ciyu.app.service.user;

import com.ciyu.app.pojo.User;

import java.util.Optional;

/*
增加功能：
1. (用户id) => 生词列表
 */
public interface UserService {
    User saveUser(User user);
    Optional<User> findUserByPhone(String phone);
    Iterable<User> findAll();

    void checkPassword(String phone, String password);
}
