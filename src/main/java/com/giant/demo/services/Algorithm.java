package com.giant.demo.services;

import com.giant.demo.entities.*;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import com.giant.demo.entities.Job;
import com.giant.demo.repositories.PrecinctRepository;
import com.giant.demo.returnreceivemodels.SimpleClusterGroups;
import com.giant.demo.returnreceivemodels.SingleClusterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Transient;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class Algorithm {

    private int gerrymanderingIndex;
    private StateE state;
    private ArrayList<ClusterEdge> candidatePairs;
    private List<Race> commmunitiesOfInterest;
    private State realState;
    private Set<Cluster> clusters;
    private Job job;
    private SimpleClusterGroups simpleClusterGroups;
    @Transient
    private Map<String, ClusterEdge> clusterEdgeMap;

    private static ConcurrentLinkedQueue<Move> moveQueue;

    @Autowired
    private PrecinctRepository precinctRepository;

    public Algorithm(){
        this.clusterEdgeMap = new HashMap<>();
        this.candidatePairs = null;
        moveQueue = new ConcurrentLinkedQueue<>();

    }

    public SimpleClusterGroups graphPartition(Set<Cluster> clusters){
        int level = 1;
        candidatePairs = new ArrayList<>();
        int end = (int) (Math.log(job.getNumDistricts())) + 1;
        int totalPop = 0;
        for(Cluster c : clusters) {
            totalPop += c.getPopulation();
            c.level = 0;
        }
        while((int) (Math.log(clusters.size()) / Math.log(2)) > end+1){
            int numClusters = clusters.size();
            for(Cluster c : clusters){
                if(c.level < level){
                    ClusterEdge candidate = findClusterPair(c, numClusters, totalPop, job);
                    if(candidate != null && candidate.getCluster2().level < level){
                        candidatePairs.add(candidate);
                        candidate.getCluster1().level = level + 1;
                        candidate.getCluster2().level = level + 1;
                    }
                }
            }
            if(candidatePairs.size() == 0)
                break;
            for(ClusterEdge edge : candidatePairs){
                edge.getCluster1().combineCluster(edge.getCluster2());
                combineEdges(edge);
                clusters.remove(edge.getCluster2());
            }
            candidatePairs = new ArrayList<>();
            level++;
        }
        Set<String> keys  = clusterEdgeMap.keySet();
        clusters = toDistrict(clusters, job.getNumDistricts());
        realState = new State();
        realState.setNumOfDistricts(job.getNumDistricts());//job.getNumDistricts());
        realState.setDistricts(clusters);
        /*Setting up SimpleClusterGroups*/
        return stateToSimpleClusterGroups(realState);
    }

    public ClusterEdge findClusterPair(Cluster c, int numClusters, int totalPop, Job j){
        double max = 0.0;
        ClusterEdge bestEdge = null;
        double popUpperBound = totalPop / ((double)numClusters / 2.0) * 1.2;
        for(String key : c.getEdgeIDs()){
            ClusterEdge e = clusterEdgeMap.get(key);
            int combinePop = e.getCluster1().getPopulation() + e.getCluster2().getPopulation();
            if(bestEdge == null || combinePop <= popUpperBound && e.getJoinability(j) > max){
                max = e.getJoinability(j);
                bestEdge = e;
            }
        }
        return bestEdge;
    }

    public void combineEdges(ClusterEdge edge){//return back to normal
        Cluster c1 = edge.getCluster1();
        Cluster c2 = edge.getCluster2();
        String ey = createKey(c1.getClusterID(), c2.getClusterID());
        c2.removeEdgeID(ey);
        clusterEdgeMap.remove(ey);
        c1.removeEdgeID(ey);
        for(String key : c2.getEdgeIDs()){
            ClusterEdge e2 = clusterEdgeMap.get(key);
            if(e2 != null) {
                if (c2.equalsC(e2.getCluster1())) {
                    String k = createKey(c1.getClusterID(), e2.getCluster2().getClusterID());
                    clusterEdgeMap.put(k, new ClusterEdge(c1, e2.getCluster2()));
                    e2.getCluster2().removeEdgeID(key);
                    e2.getCluster2().addEdgeID(k);
                    c1.addEdgeID(k);
                } else {
                    String k = createKey(c1.getClusterID(), e2.getCluster1().getClusterID());
                    clusterEdgeMap.put(k, new ClusterEdge(c1, e2.getCluster1()));
                    e2.getCluster1().removeEdgeID(key);
                    e2.getCluster1().addEdgeID(k);
                    c1.addEdgeID(k);
                }
            }
            clusterEdgeMap.remove(key);
        }
    }


    //makes sure the number of districts equals the number specified
    public Set<Cluster> toDistrict(Set<Cluster> clusters, int numOfDistricts){
        while(clusters.size() != numOfDistricts){
            Cluster breakdown = minPopulation(clusters);  /* <---- this returns null*/
            System.out.println(breakdown.getEdgeIDs());
            breakCluster(breakdown);
            for(String key : breakdown.getEdgeIDs()){
                clusterEdgeMap.remove(key);
            }
            clusters.remove(breakdown);
        }
        int i = 0;
        for(Cluster c : clusters){
            c.setClusterID(i++);/*Normalizing data.*/
        }
        return clusters;
    }

    //send each precinct to neighbor with lowest population
    public void breakCluster(Cluster c){
        for(Precinct p : c.getContainedPrecincts()){
            Cluster neighbor = eligibleCluster(c);
            neighbor.addPrecinct(p);
        }
    }

    //finds adjacent cluster with smallest population
    public Cluster eligibleCluster(Cluster c){
        Cluster min = null;//terEdgeMap.get(c.getEdgeIDs());
        int pop = 0;
        for(String key : c.getEdgeIDs()){
            ClusterEdge e = clusterEdgeMap.get(key);

            Cluster c2 = e.getCluster1();
            if(c.equals(c2))
                c2 = e.getCluster2();
            if(min == null || pop > c2.getPopulation()){
                pop = c2.getPopulation();
                min = c2;
            }
        }
        return min;
    }

    //returns cluster with min population
    public Cluster minPopulation(Set<Cluster> clusters){
        int min = 0;
        Cluster ret = null;
        for(Cluster c : clusters){
            if(min == 0 || min > c.getPopulation()){
                ret = c;
                min = c.getPopulation();
            }
        }
        return ret;
    }


    public String createKey(int id1, int id2){
        int min, max;
        if(id1 <= id2){
            min = id1;
            max = id2;
        }
        else{
            min = id2;
            max= id1;
        }
        return min + "," + max;
    }


    public void generateMoves(){
        boolean foundMove = true;
        int move_count = 0;
        int max_attempts = 10000;
        while (foundMove && move_count < max_attempts){
            foundMove = false;
            Cluster worst_district = getWorstDistrict(realState);
            List<Precinct> borderPrecincts = getBorderingPrecincts(worst_district);
            Iterator<Precinct> iterator = borderPrecincts.listIterator();
            while (iterator.hasNext()){
                Precinct precinct = iterator.next();
                Set<Precinct> neighbours = precinct.getNeighbours();
                Iterator<Precinct> setIterator = neighbours.iterator();
                while (setIterator.hasNext()){
                    Precinct neighour = setIterator.next();
                    if (precinct.getCluster().getClusterID() != neighour.getCluster().getClusterID()){
                        Move move1 = new Move(precinct, precinct.getCluster(), neighour.getCluster());
                        if (testMove(move1)){ /* */
                            //excuteMove(move1); /* executed in testMove*/
                            foundMove = true;
                            moveQueue.add(move1);
                            break;
                        }else {
                            Move move2 = new Move(neighour, neighour.getCluster(), precinct.getCluster());
                            if (testMove(move2)){
                                //excuteMove(move2);
                                foundMove = true;
                                moveQueue.add(move2);
                            }
                        }
                    }

                    if (foundMove == true){
                        break; /*break inner while loop*/
                    }
                }
            }
        }
    }




    public int getGerrymanderingIndex() {
        return gerrymanderingIndex;
    }

    public void setGerrymanderingIndex(int gerrymanderingIndex) {
        this.gerrymanderingIndex = gerrymanderingIndex;
    }

    public StateE getState() {
        return state;
    }

    public void setState(StateE state) {
        this.state = state;
    }

    public ArrayList<ClusterEdge> getCandidatePairs() {
        return candidatePairs;
    }

    public void setCandidatePairs(ArrayList<ClusterEdge> candidatePairs) {
        this.candidatePairs = candidatePairs;
    }

    public List<Race> getCommmunitiesOfInterest() {
        return commmunitiesOfInterest;
    }

    public void setCommmunitiesOfInterest(List<Race> commmunitiesOfInterest) {
        this.commmunitiesOfInterest = commmunitiesOfInterest;
    }

    /*Initialize all precinct into clusters*/
    public void initializeClusters(){
        List<Precinct> allPrecinct =  precinctRepository.findAllByState(job.getStateE());
        System.out.println("allPrecincts: " + allPrecinct.size());
        System.out.println(allPrecinct.get(1).toString());
        List<Cluster> clusterList = new ArrayList<>();
        for (int i=0; i<allPrecinct.size(); i++){
            List<Precinct> precinctsList = new ArrayList<>();
            precinctsList.add(allPrecinct.get(i));
            Cluster cluster = new Cluster(allPrecinct.get(i).getPrecinctID(), precinctsList);
            cluster.getContainedPrecincts().get(0).setCluster(cluster);
            clusterList.add(cluster);
        }
        initializeEdges(clusterList, allPrecinct);
        Set<Cluster> ret = new HashSet<>(clusterList);
        this.clusters = ret;
    }

    public void initializeEdges(List<Cluster> clusters, List<Precinct> precincts){
        Map<Integer, Cluster>  tempC = new HashMap<>();
        for(int i = 0; i < clusters.size(); i++){
            tempC.put(clusters.get(i).getClusterID(), clusters.get(i));
        }
        for(int i = 0; i < precincts.size(); i++){
            for(Precinct p : precincts.get(i).getNeighbours()){
                int ID = p.getPrecinctID();
                //clusters.get(i).getEdges().add();
                //clusters.get(i).getClusterEdgeMap().put(ID, new ClusterEdge(clusters.get(i), tempC.get(ID)));
                String key = createKey(clusters.get(i).getClusterID(), ID);
                clusterEdgeMap.put(key, new ClusterEdge(clusters.get(i), tempC.get(ID)));
                clusters.get(i).addEdgeID((key));
            }
        }
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Set<Cluster> getClusters() {
        return clusters;
    }

    private SimpleClusterGroups stateToSimpleClusterGroups(State realState){
        SimpleClusterGroups groups = new SimpleClusterGroups();
        Set<Cluster> districts = realState.getDistricts();
        Iterator<Cluster> iterator = districts.iterator();
        while(iterator.hasNext()){
            Cluster district = iterator.next();
            groups.addClusterGroup(districtToSingleClusterGroup(district));
        }
        return groups;
    }

    private SingleClusterGroup districtToSingleClusterGroup(Cluster district){
        SingleClusterGroup singleClusterGroup = new SingleClusterGroup(district.getClusterID());
        List<Precinct> precincts = district.getContainedPrecincts();
        ListIterator<Precinct> iterator = precincts.listIterator();
        while(iterator.hasNext()){
            Precinct precinct = iterator.next();
            singleClusterGroup.addPrecinctID(precinct.getPrecinctID());
        }
        return singleClusterGroup;
    }



    public List<Precinct> getBorderingPrecincts(Cluster c){
        List<Precinct> precincts = new LinkedList<>();
        for(Precinct p : c.getContainedPrecincts()){
            if(p.getBoundaries().touches(c.getBoundary().getBoundary()))
                precincts.add(p);
        }
        return precincts;
    }

    private Cluster getWorstDistrict(State realState) {
        Cluster worstDistrict = null;
        double minScore = Double.POSITIVE_INFINITY;
        for (Cluster cluster : realState.getDistricts()){
            double score = cluster.getObjectiveFunction().getScore(job); /*getScore needs to be fixed.*/
            if (score < minScore){
                worstDistrict = cluster;
                minScore = score;
            }
        }

        return worstDistrict;
    }



    /*

    public Move getMoveFromDistrict(Cluster startDistrict){
        Set<Precinct> precincts = null;
        for(Precinct p : precincts){
            for(Precinct n : p.getNeighbours()){
                Precinct neighbor = realState.getCluster(n.getClusterID());
                if(!c.getContainedClusters().contains(neighbor)){
                    Cluster neighbor = realState.findCluster(temp);
                    Move move = testMove(neighbor, startDistrict, p);
                    if(move != null){
                        currDistrict = startDistrict;
                        return move;
                    }
                    move = testMove(startDistrict, neighbor, neighbor);
                    if(move != null){
                        currDistrict = startDistrict;
                        return move;
                    }
                }
            }
        }
        return null;
    }*/

    private void excuteMove(Move move1) {
        Cluster from = move1.getFrom();
        Cluster to = move1.getTo();
        Precinct precinct = move1.getPrecinct();

        from.getContainedPrecincts().remove(precinct); /*Make method inside Cluster.*/
        /*
         * geometry operation
         * population operation
         * demographics operation
         * party preferences.
         *
         */

        to.getContainedPrecincts().add(precinct);

        /*
         * geometry operation
         * population operation
         * demographics operation
         * party preferences.
         *
         */

        /*change reflected inside the realState. So the state */
    }

    private boolean testMove(Move move1) {
        Cluster from = move1.getFrom();
        Cluster to = move1.getTo();
        Precinct precinct = move1.getPrecinct();
        /*Normalize here*/
        from.getObjectiveFunction().normalizedObjectiveFunction();
        to.getObjectiveFunction().normalizedObjectiveFunction();
        double originalScore = from.getObjectiveFunction().getScore(job)+to.getObjectiveFunction().getScore(job);
        excuteMove(move1);
        /*Update objective function*/

        double fromScore = from.rateDistrict(); /*need to be implemented*/
        double toScore = to.rateDistrict();

        double finalScore = fromScore + toScore;
        double change = finalScore - originalScore;
        if (change <= 0){
            /*undo*/
            Move undo = new Move(precinct, to, from);
            excuteMove(undo);
            return false;
        }

        return true;
    }

}
