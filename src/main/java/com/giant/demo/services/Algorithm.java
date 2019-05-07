package com.giant.demo.services;

import com.giant.demo.entities.*;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Algorithm {

    private int gerrymanderingIndex;
    private StateE state;
    private ArrayList<ClusterEdge> candidatePairs;
    private List<Race> commmunitiesOfInterest;
    private State realState;

    public Algorithm(){
        this.candidatePairs = null;
    }

    public Set<Cluster> GraphPartition(Set<Cluster> clusters){
        int level = 0;
        candidatePairs = new ArrayList<ClusterEdge>();//list to hold pairs based off joinability
        int start = (int) (Math.log(clusters.size()) / Math.log(2));//number of pairings needed to partition
        int end = (int) (Math.log(this.realState.getNumOfDistricts()));//number desired in logs
        for(int i =  start; i > end; i--){
            for(Cluster c : clusters){
                if(c.level < level){//only pair each cluster once per iteration
                    ClusterEdge candidate = c.findClusterPair(clusters.size(), realState.getPopulation());
                    if(candidate != null){
                        candidatePairs.add(candidate);
                    }
                }
            }
            //When all the candidate pairs are picked they are then evaluated
            for(ClusterEdge edge : candidatePairs){
                edge.getCluster1().combineCluster(edge.getCluster2());
                clusters.remove(edge.getCluster2());//second cluster removed
                edge.getCluster1().level = level;
            }
            level++;
        }
        realState.setDistricts(clusters);
        realState.toDistrict();//makes sure the number of districts is the desired amount
        return realState.getDistricts();
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


}
