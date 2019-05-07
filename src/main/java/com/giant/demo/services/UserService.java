package com.giant.demo.services;

import com.giant.demo.entities.User;
import com.giant.demo.enums.UserType;
import com.giant.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User save(User newUser) {
        /*To see if duplicate username is found*/
        User user = userRepository.findByUsername(newUser.getUsername());
        if (user != null){
            return null;
        }
        String password = bCryptPasswordEncoder.encode(newUser.getPassword());
        newUser.setPassword(password);
        newUser.setUserType(UserType.REGULAR);
        return userRepository.save(newUser);
    }
}
