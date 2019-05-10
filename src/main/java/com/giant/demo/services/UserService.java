package com.giant.demo.services;

import com.giant.demo.entities.Salt;
import com.giant.demo.entities.User;
import com.giant.demo.enums.UserType;
import com.giant.demo.repositories.SaltRepository;
import com.giant.demo.repositories.UserRepository;
import com.giant.demo.returnreceivemodels.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SaltRepository saltRepository;
    private static BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    public User save(@Valid UserModel newUser) {
        /*To see if duplicate username is found*/
        User user = userRepository.findByUsername(newUser.getUsername());
        if (user != null){
            return null;
        }
        String password = bCryptPasswordEncoder.encode(newUser.getPassword());
        User actualUser = new User();
        actualUser.setPassword(password);
        actualUser.setUserType(UserType.REGULAR);
        Salt salt = new Salt(newUser.getSalt());
        saltRepository.save(salt);
        actualUser.setSalt(salt);
        actualUser.setUsername(newUser.getUsername());
        return userRepository.save(actualUser);
    }

    public Salt getSalt(String username) throws Exception{
        try{
            return userRepository.findByUsername(username).getSalt();
        }catch (Exception e){
            throw new Exception();
        }
    }

    public User getUser(String username){
        return userRepository.findByUsername(username);
    }
}
