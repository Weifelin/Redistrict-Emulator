package com.giant.demo.repositories;

import com.giant.demo.entities.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class FakeDB {

    private HashMap<String, User> users = new HashMap<>();


    public User getUser(String username){
        return users.get(username);
    }

    public boolean addUser(User user){
        users.put(user.getUsername(), user);

        return true; //hard coded return value.
    }

}
