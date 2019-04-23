package com.giant.demo.entities;

import com.giant.demo.enums.StateE;

import java.util.Set;

public class State {
    private StateE state;
    private int numOfDistricts;
    private Set<Cluster> districts;
    private Set<Cluster> majorityMinorityDistricts;

    public State() {
    }

    public State(StateE state, int numOfDistricts, Set<Cluster> districts) {
        this.state = state;
        this.numOfDistricts = numOfDistricts;
        this.districts = districts;
    }

    public void breakCluster(Cluster c){

    }

    public void displayMajorityMinorityDistricts(){

    }


    public StateE getState() {
        return state;
    }

    public void setState(StateE state) {
        this.state = state;
    }

    public int getNumOfDistricts() {
        return numOfDistricts;
    }

    public void setNumOfDistricts(int numOfDistricts) {
        this.numOfDistricts = numOfDistricts;
    }

    public Set<Cluster> getDistricts() {
        return districts;
    }

    public void setDistricts(Set<Cluster> districts) {
        this.districts = districts;
    }

    public Set<Cluster> getMajorityMinorityDistricts() {
        return majorityMinorityDistricts;
    }

    public void setMajorityMinorityDistricts(Set<Cluster> majorityMinorityDistricts) {
        this.majorityMinorityDistricts = majorityMinorityDistricts;
    }
}
