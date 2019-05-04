package com.giant.demo.services;

import com.giant.demo.entities.User;
import com.giant.demo.repositories.FakeDB;
//import com.giant.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private FakeDB fakeDB;
    //private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

//        User user = userRepository.findByUsername(username);
//        if (user == null) throw new UsernameNotFoundException(username);
        User user = fakeDB.getUser(username);

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
