package com.ciyu.app.repository;

import com.ciyu.app.pojo.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByPhone(String phone);
}
