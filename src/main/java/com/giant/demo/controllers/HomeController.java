package com.giant.demo.controllers;


import com.giant.demo.entities.Job;
import com.giant.demo.entities.Salt;
import com.giant.demo.entities.User;
import com.giant.demo.preprocessing.PreProcess;
import com.giant.demo.returnreceivemodels.SimpleClusterGroups;
import com.giant.demo.returnreceivemodels.UserModel;
import com.giant.demo.services.Algorithm;
import com.giant.demo.services.BatchService;
import com.giant.demo.services.SecurityService;
import com.giant.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private PreProcess preProcess;

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

        preProcess.loadPrecincts();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

//    @GetMapping("/batch")
//    public BatchSummary getBatchResult(){
//        return batchService.getBatchSummary();
//    }

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserModel newUser, HttpServletRequest httpServletRequest){
        User user = userService.save(newUser);
        if (user != null) {
            securityService.autoLogin(user.getUsername(), newUser.getPassword(), httpServletRequest);
        }
        return user; //show single batch.
    }


    @PostMapping("/single-run")
    public SimpleClusterGroups singleRun(@RequestBody Job job){
        algorithm.setJob(job); /*Store job in algorithm until phase II. */
        algorithm.initializeClusters();
        return algorithm.graphPartition(algorithm.getClusters());
    }

    @GetMapping(value = "/{username}/salt")
    public Salt getSalt(@PathVariable("username") String username){
        User user = userService.getUser(username);
        try {
            System.out.println("username: "+username);
            System.out.println("salt:"+user.getSalt().getSaltString());
            return userService.getSalt(username);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Doesn't Exist", e);
        }
    }

}
