package com.giant.demo.controllers;


import com.giant.demo.entities.User;
import com.giant.demo.services.SecurityService;
import com.giant.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
//@ComponentScan("/resources")
public class HomeController {
//    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10000);
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

    //Login is processed by WebSecurityConfig
    /*/login POST controller is provided by Spring Security*/
    @GetMapping("/login")
    public String login(Model model, String error, String logout){
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody User newUser, HttpServletRequest httpServletRequest){


        User user = userService.save(newUser);
        if (user != null) {
            securityService.autoLogin(newUser.getUsername(), newUser.getPassword(), httpServletRequest);
        }

        return "redirect:/single-batch"; //show single batch.
    }

    @GetMapping("/error")
    public String error(){
        return "redirect:/";
    }



}
