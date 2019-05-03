package com.giant.demo.entities;

import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;

import java.util.ArrayList;
import java.util.List;

public class Algorithm {

    private int gerrymanderingIndex;
    private StateE state;
    private ArrayList<Cluster[]> candidatePairs;
    private List<Race> commmunitiesOfInterest;

    public Algorithm(){

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

    public ArrayList<Cluster[]> getCandidatePairs() {
        return candidatePairs;
    }

    public void setCandidatePairs(ArrayList<Cluster[]> candidatePairs) {
        this.candidatePairs = candidatePairs;
    }

    public List<Race> getCommmunitiesOfInterest() {
        return commmunitiesOfInterest;
    }

    public void setCommmunitiesOfInterest(List<Race> commmunitiesOfInterest) {
        this.commmunitiesOfInterest = commmunitiesOfInterest;
    }
}
