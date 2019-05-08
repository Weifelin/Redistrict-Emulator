package com.giant.demo.controllers;


import com.giant.demo.entities.BatchSummary;
import com.giant.demo.entities.User;
import com.giant.demo.services.BatchService;
import com.giant.demo.services.SecurityService;
import com.giant.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;

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




//    @GetMapping("/error")
//    public String handleError(HttpServletRequest request){
//        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//        if (status != null){
//            Integer statusCode = Integer.valueOf(status.toString());
//            if (statusCode == HttpStatus.UNAUTHORIZED.value()){
//                return "index";
//            }
//        }
//        return "error";
//    }




}
