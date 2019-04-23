package com.giant.demo.services;

import com.giant.demo.entities.User;

public interface UserService {
    void save(User user);

    User findByUsername(String username);
}
