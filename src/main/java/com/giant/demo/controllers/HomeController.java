package com.giant.demo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.giant.demo.entities.Job;
import com.giant.demo.enums.AlgorithmStatus;
import com.giant.demo.enums.StateE;
import com.giant.demo.jobObjects.*;
import com.giant.demo.entities.Salt;
import com.giant.demo.entities.User;
import com.giant.demo.preprocessing.PreProcess;
import com.giant.demo.returnreceivemodels.SimpleClusterGroups;
import com.giant.demo.returnreceivemodels.UserModel;
import com.giant.demo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.io.File;
import java.io.IOException;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;


@RestController
public class HomeController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private Algorithm algorithm;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private PreProcess preProcess;
    @Autowired
    private GeoJsonService geoJsonService;

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


//        preProcess.loadPrecincts();
//        geoJsonService.createGeoJson();

        //generateJobJson();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    private void generateJobJson() {
        Compactness compactness = new Compactness(0.5, 0.1,0.2,0.3);
        AfricanAmerican africanAmerican = new AfricanAmerican(34, 45);
        Asian asian = new Asian(33, 37);
        LatinAmerican latinAmerican = new LatinAmerican(33, 77);
        Job job = new Job(compactness, 10, 0.5,0.6,0.7,4,africanAmerican,latinAmerican,asian, StateE.NJ);
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(new File("job.json"), job);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @GetMapping("/batch")
//    public BatchSummary getBatchResult(){
//        return batchService.getBatchSummary();
//    }

    @CrossOrigin
    @PostMapping("/register")
    public UserModel register(@Valid @RequestBody UserModel newUser, HttpServletRequest httpServletRequest){
        User user = userService.save(newUser);
        if (user != null) {
            securityService.autoLogin(user.getUsername(), newUser.getPassword(), httpServletRequest);
        }
        return new UserModel(user.getUsername(), user.getUserType(), user.getPassword(), user.getSalt().getSaltString()); //show single batch.
    }


    @CrossOrigin
    //@PostMapping("/login")
    @RequestMapping(value="/login-process", method=RequestMethod.POST)
    public UserModel login(@Valid @RequestBody UserModel userModel, HttpServletRequest request, HttpServletResponse response){

        String username = userModel.getUsername();
        String password = userModel.getPassword();
        UserRules userRules = (UserRules) userDetailsService.loadUserByUsername(username);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (bCryptPasswordEncoder.matches(password, userRules.getPassword())){
            UsernamePasswordAuthenticationToken authReq
                    = new UsernamePasswordAuthenticationToken(username, userRules.getPassword());
            Authentication auth = authenticationProvider.authenticate(authReq);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(auth);
            HttpSession session = request.getSession(true);
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);
            UserModel ret = new UserModel(username, userModel.getUserType(), "PASS", userModel.getSalt());
            return ret;
        }

        try {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Login Error");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new UserModel();
    }

    @CrossOrigin
    //@PostMapping("/logout")
    @RequestMapping(value="/logout-process", method=RequestMethod.POST)
    public HttpStatus logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // concern you

        User currUser = userService.getUser(auth.getName()); // some of DAO or Service...

        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler(); // concern you

        if( currUser == null ){
            ctxLogOut.logout(request, response, auth); // concern you
        }

        return HttpStatus.ACCEPTED;
    }


    @PostMapping("/single-run")
    public SimpleClusterGroups singleRun(@RequestBody Job job){
//        counter ++;
//        System.out.println("being called: "+ counter);
        if (algorithm.getStatus() == AlgorithmStatus.Running){
            return null;
        }

        if (job == null){
            return  null;
        }

        algorithm.lockAlgorithm();
        algorithm.setJob(job); /*Store job in algorithm until phase II. */
        algorithm.initializeClusters();
        SimpleClusterGroups simpleClusterGroups = algorithm.graphPartition(algorithm.getClusters());
        System.out.println("simple: "+ simpleClusterGroups.getState().toString());
        return simpleClusterGroups;
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
