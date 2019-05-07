package com.giant.demo.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@RestController
public class AppErrorController implements ErrorController {
    private final static String ERR_PATH = "/error";
    @Override
    public String getErrorPath() {
        return ERR_PATH;
    }

    @GetMapping("/error")
    public ModelAndView handleError(HttpServletRequest request, ModelMap model) {
        //do something like logging
        ModelAndView modelAndView;
        //model.addAttribute("attribute", "error");
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null){
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                modelAndView = new ModelAndView(new RedirectView("/"));
                return modelAndView;
            }

        }

        return new ModelAndView("error");
    }
}
