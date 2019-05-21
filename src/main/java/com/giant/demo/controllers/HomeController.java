package com.giant.demo.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.giant.demo.entities.*;
import com.giant.demo.enums.AlgorithmStatus;
import com.giant.demo.enums.StateE;
import com.giant.demo.jobObjects.*;
import com.giant.demo.preprocessing.PreProcess;
import com.giant.demo.returnreceivemodels.*;
import com.giant.demo.services.*;
import org.locationtech.jts.geom.Geometry;
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
import java.util.concurrent.ConcurrentLinkedQueue;

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
//
//        if (job == null){
//            return  null;
//        }

        algorithm.lockAlgorithm();
//        job.setPopulationEquality(Math.abs(1.0-job.getPopulationEquality()));
        algorithm.setJob(job); /*Store job in algorithm until phase II. */
        algorithm.initializeClusters();
        SimpleClusterGroups simpleClusterGroups = algorithm.graphPartition(algorithm.getClusters());
        System.out.println("simple: "+ simpleClusterGroups.getState().toString());
        algorithm.initializeClusterQueue();
//        State realState = algorithm.getRealState();
//        int numOfPrecinct = 0;
//        for (Cluster cluster: realState.getDistricts()){
//            numOfPrecinct += cluster.getContainedPrecincts().size();
//        }
//
//        System.out.println("Total Precincts: " + numOfPrecinct);

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

    @PostMapping("/start-simulatedAnnealing")
    public MoveModel trigger(@RequestBody Job job){
        algorithm.initializeObjectiveFunction();
        algorithm.initializeObjectiveFunctionMap();
        algorithm.generateMoves();
        Move move = new Move();
        move.setFinished(true);
        move.setFrom(new Cluster(-1));
        move.setTo(new Cluster(-1));
        move.setPrecinct(new Precinct());
        algorithm.getMoveQueue().add(move);
        return null; /* This will be returned after generateMoves() finishes.*/
    }

    @GetMapping("/getmoves")
    public MoveModel getMove(){
        ConcurrentLinkedQueue<Move> queue =  algorithm.getMoveQueue();
        Move move = queue.poll();
        if (move != null) {
            MoveModel moveModel = new MoveModel(move.getFrom().getClusterID(),
                                                move.getTo().getClusterID(),
                                                move.getPrecinct().getPrecinctID(), move.isFinished());
            return moveModel;
        }

        return null;
    }

    @GetMapping("/getClusters")
    public ClusterModel getCluster(){
        ConcurrentLinkedQueue<Cluster> clusters = algorithm.getClustersQueue();
        Cluster cluster = clusters.poll();
        if (cluster!= null){
//            System.out.println("Printing Boundaries");
//            System.out.println(geoJsonService.coordinatesArrayToString(cluster.getBoundary().getCoordinates()));
            ClusterModel clusterModel = new ClusterModel(cluster.getClusterID(),
                    cluster.getBoundary(),
                    cluster.getPopulation(),
                    cluster.getDemographics(),
                    cluster.getPartyPreference(),
                    cluster.isMajorityMinority(),
                    cluster.getNumDemo(),
                    cluster.getNumRep(),
                    cluster.getVotes());
            return clusterModel;
        }

        return null;
    }

    @GetMapping("/getSummary")
    public SummaryModel getSummary(){
        /*Construct Summary Object from realState*/
        State realState = algorithm.getRealState();
        if (realState != null) {
            return new SummaryModel(realState);
        }

        return null;
    }

    @PostMapping("/saveMap")
    public HttpStatus saveMap(@RequestBody UserModel userModel){
        User user = userService.getUser(userModel.getUsername());
        user.addState(algorithm.getRealState());
        return HttpStatus.ACCEPTED;
    }


}
