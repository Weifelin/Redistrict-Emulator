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

    private static ConcurrentLinkedQueue<Move> moveQueue;

    @Autowired
    private PrecinctRepository precinctRepository;

    public Algorithm(){
        this.candidatePairs = null;
        moveQueue = new ConcurrentLinkedQueue<>();
    }

    public SimpleClusterGroups graphPartition(Set<Cluster> clusters){
        int level = 0;
        candidatePairs = new ArrayList<ClusterEdge>();
        int start = (int) (Math.log(clusters.size()) / Math.log(2));
        int end = (int) (Math.log(12));//return back to normal - job.getNumDistricts()));
        int totalPop = 0;
        for(Cluster c : clusters)
            totalPop += c.getPopulation();
        for(int i =  start; i > end; i--){
            int numClusters = clusters.size();
            for(Cluster c : clusters){
                if(c.level < level){
                    ClusterEdge candidate = c.findClusterPair(numClusters, totalPop, job);

                    if(candidate != null){
                        candidatePairs.add(candidate);
                    }
                }
            }
            for(ClusterEdge edge : candidatePairs){
                Cluster temp = edge.getCluster2();
                clusters.remove(edge.getCluster2());
                edge.getCluster1().combineCluster(temp);
                edge.getCluster1().level = level;
            }
            candidatePairs = new ArrayList<ClusterEdge>();
            level++;
            System.out.println("Number of Clusters: " + clusters.size());
        }
        System.out.println("cluster #: " + clusters.size());
        realState = new State();
        realState.setNumOfDistricts(12);//job.getNumDistricts());
        realState.setDistricts(clusters);
        realState.toDistrict();
        /*Setting up SimpleClusterGroups*/
        return stateToSimpleClusterGroups(realState);
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
                            excuteMove(move1); /**/
                            foundMove = true;
                            moveQueue.add(move1);
                            break;
                        }else {
                            Move move2 = new Move(neighour, neighour.getCluster(), precinct.getCluster());
                            if (testMove(move2)){
                                excuteMove(move2);
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
        List<Precinct> allPrecinct =  precinctRepository.findAll();
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
                clusters.get(i).getEdges().add(new ClusterEdge(clusters.get(i), tempC.get(ID)));

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
    }

    private boolean testMove(Move move1) {
        Cluster from = move1.getFrom();
        Cluster to = move1.getTo();

        double originalScore = from.getObjectiveFunction().getScore(job)+to.getObjectiveFunction().getScore(job);



        return false;
    }
}
