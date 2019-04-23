package com.giant.demo.services;

import com.giant.demo.entities.User;
import com.giant.demo.repositories.FakeDB;
//import com.giant.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private FakeDB fakeDB;
    //private UserRepository userRepository;
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public void save(User user) {
        //user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setPassword(user.getPassword());
        fakeDB.addUser(user);
        //userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return fakeDB.getUser(username);
        //return userRepository.findByUsername(username);
    }
}
