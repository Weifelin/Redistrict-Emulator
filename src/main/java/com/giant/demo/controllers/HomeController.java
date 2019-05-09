package com.giant.demo.controllers;


import com.giant.demo.entities.User;
import com.giant.demo.services.Algorithm;
import com.giant.demo.services.BatchService;
import com.giant.demo.services.SecurityService;
import com.giant.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;


@RestController
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private Algorithm algorithm;

    private BatchService batchService;

    /*/login POST controller is provided by Spring Security*/
//    @GetMapping("/login")
//    public ModelAndView login(){
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("login");
//        return modelAndView;
//    }

    @GetMapping("/")
    public ModelAndView index(){
        //return new ModelAndView("index");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

//    @GetMapping("/batch")
//    public BatchSummary getBatchResult(){
//        return batchService.getBatchSummary();
//    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody User newUser, HttpServletRequest httpServletRequest){
        User user = userService.save(newUser);
        if (user != null) {
            securityService.autoLogin(newUser.getUsername(), newUser.getPassword(), httpServletRequest);
        }

        return user; //show single batch.
    }





}
