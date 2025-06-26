package com.github.vyhovskyi.dao;

import com.github.vyhovskyi.entity.User;

import java.util.Optional;

public interface UserDao extends AutoCloseable {
    Optional<User> getUserByUsername(String username);
    void saveUser(User user);
    void deleteUser(User user);
    void close();
}
