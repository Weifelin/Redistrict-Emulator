package com.giant.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/login")
    public String login(){
        return "login";
    }


    @RequestMapping("/submit")
    public String submit(){
        return "";
    }

}
