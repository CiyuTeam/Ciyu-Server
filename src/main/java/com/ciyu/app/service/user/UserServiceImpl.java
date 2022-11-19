package com.ciyu.app.service.user;

import com.ciyu.app.exception.PhonePasswordNotMatch;
import com.ciyu.app.pojo.User;
import com.ciyu.app.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @RequiredArgsConstructor @Transactional @Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepo userRepo;
    @Override
    public User saveUser(User user) {
        log.info("Saving new user {} to the database", user.getPhone());
        return userRepo.save(user);
    }

    @Override
    public Optional<User> findUserByPhone(String phone) {
        log.info("Fetching user {}", phone);
        return userRepo.findByPhone(phone);
    }

    @Override
    public Iterable<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public void checkPassword(String phone, String password) {
        User user = findUserByPhone(phone).orElseThrow(PhonePasswordNotMatch::new);
        if (user.getPassword().equals(password))
            return;
        throw new PhonePasswordNotMatch();
    }
}
