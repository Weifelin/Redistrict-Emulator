package com.giant.demo.controllers;



import com.giant.demo.entities.User;
import com.giant.demo.services.SecurityService;
import com.giant.demo.services.UserService;
import com.giant.demo.services.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class HomeController {

    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10000);

    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private UserValidator userValidator;


    /*/login POST controller is provided by Spring Security*/
    @GetMapping("/login")
    public String login(Model model, String error, String logout){
        if (error != null)
            model.addAttribute("error", "Invalid password or username");

        if (logout != null)
            model.addAttribute("message", "Logged out");

        return "redirect:/single-batch"; // show single batch run
    }


    @PostMapping("/register")
    public String register(@ModelAttribute("newUser") User newUser, BindingResult bindingResult){
        userValidator.validate(newUser, bindingResult);

        if (bindingResult.hasErrors()){
            return "register error";
        }

        userService.save(newUser);

        securityService.autoLogin(newUser.getUserName(), newUser.getPassword());

        return "redirect:/single-batch"; //show single batch.
    }





}
