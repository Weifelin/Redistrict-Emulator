package com.giant.demo.services;

import com.giant.demo.entities.*;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import com.giant.demo.repositories.PrecinctRepository;
import com.giant.demo.returnreceivemodels.SimpleClusterGroups;
import com.giant.demo.returnreceivemodels.SingleClusterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private PrecinctRepository precinctRepository;

    public Algorithm(){
        this.candidatePairs = null;
    }

    public SimpleClusterGroups graphPartition(Set<Cluster> clusters){
        int level = 0;
        candidatePairs = new ArrayList<ClusterEdge>();
        int start = (int) (Math.log(clusters.size()) / Math.log(2));
        int end = (int) (Math.log(this.realState.getNumOfDistricts()));
        for(int i =  start; i > end; i--){
            for(Cluster c : clusters){
                if(c.level < level){
                    ClusterEdge candidate = c.findClusterPair(clusters.size(), realState.getPopulation());
                    if(candidate != null){
                        candidatePairs.add(candidate);
                    }
                }
            }
            for(ClusterEdge edge : candidatePairs){
                edge.getCluster1().combineCluster(edge.getCluster2());
                clusters.remove(edge.getCluster2());
                edge.getCluster1().level = level;
            }
            level++;
        }
        realState.setDistricts(clusters);
        realState.toDistrict();
        /*Setting up SimpleClusterGroups*/
        return stateToSimpleClusterGroups(realState);
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
        Set<Cluster> clusters = new HashSet<>();
        for (int i=0; i<allPrecinct.size(); i++){
            ArrayList<Precinct> precinctsList = new ArrayList<>();
            precinctsList.add(allPrecinct.get(i));
            clusters.add(new Cluster(allPrecinct.get(i).getPrecinctID(), precinctsList));
        }
        this.clusters = clusters;
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

}
