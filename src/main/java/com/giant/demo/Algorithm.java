package com.giant.demo;

import com.giant.demo.entities.Cluster;
import com.giant.demo.entities.ClusterEdge;
import com.giant.demo.entities.Move;
import com.giant.demo.entities.Precinct;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Algorithm {
    private int gerrymanderingIndex;
    private StateE state;
    private Set<Cluster> clusters;
    private Graph<Cluster, ClusterEdge> map;
    private ArrayList<Cluster[]> candidatePairs;
    private List<Race> communtiesOfInterest;

    public Algorithm() {
    }


    public boolean initalize(){
        return false;
    }

    public boolean phase1(){
        return false;
    }

    public boolean phase2(){
        return false;
    }

    public boolean updateOF(Move move){
        return false;
    }

    public void  normalizeOF(Set<Cluster> clusters){

    }

    public Set<Precinct> selectPrecinct(){
        return null;
    }

    public boolean shouldCombine(Cluster c1, Cluster c2){
        return false;
    }

    public void combineClusters(Cluster c1, Cluster c2){

    }

    public boolean mergeBorder(Cluster c1, Cluster c2){
        return false;
    }

    public boolean updateEdge(Graph map){
        return false;
    }

    public boolean sendPrecinct(Precinct p, int c1ID, int c2ID){
        return false;
    }

    public int sendCluster(int clusterID){
        return 0;
    }

    /*
    *
    * methods:
    *
    *  sendSummary,
    *  run
    *  saveMap
    *
    *  -------------------------
    * class:
    *   BatchRun
    *   Job
    *   Summary
    * */




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

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Set<Cluster> clusters) {
        this.clusters = clusters;
    }

    public Graph<Cluster, ClusterEdge> getMap() {
        return map;
    }

    public void setMap(Graph<Cluster, ClusterEdge> map) {
        this.map = map;
    }

    public ArrayList<Cluster[]> getCandidatePairs() {
        return candidatePairs;
    }

    public void setCandidatePairs(ArrayList<Cluster[]> candidatePairs) {
        this.candidatePairs = candidatePairs;
    }

    public List<Race> getCommuntiesOfInterest() {
        return communtiesOfInterest;
    }

    public void setCommuntiesOfInterest(List<Race> communtiesOfInterest) {
        this.communtiesOfInterest = communtiesOfInterest;
    }
}
