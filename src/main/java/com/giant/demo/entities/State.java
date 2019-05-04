package com.giant.demo.entities;

import com.giant.demo.enums.StateE;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Set;

@Entity
public class State {
    @Id
    private int stateID;
    private StateE state;
    private int numOfDistricts;
    private Set<Cluster> districts;
    private Set<Cluster> majorityMinorityDistricts;
    private int population;
    private Demographics demographics;


    public State() {
    }

    public State(StateE state, int numOfDistricts, Set<Cluster> districts) {
        this.state = state;
        this.numOfDistricts = numOfDistricts;
        this.districts = districts;
    }

    public void breakCluster(Cluster c){
        for(Precinct p : c.getContainedPrecincts()){
            Cluster neighbor = eligibleCluster(c);
            neighbor.addPrecinct(p);
        }
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

    public Cluster minPopulation(){
        int min = 0;
        Cluster ret = null;
        for(Cluster c : this.districts){
            if(min == 0 || min > ret.getPopulation()){
                ret = c;
                min = ret.getPopulation();
            }
        }
        return ret;
    }

    public Cluster eligibleCluster(Cluster c){
        Cluster min = null;
        for(ClusterEdge e : c.getEdges()){
            if(min == null || min.getPopulation() < e.getCluster2().getPopulation()){
                min = e.getCluster2();
            }
        }
        return min;
    }

    public void toDistrict(){
        while(this.districts.size() != numOfDistricts){
            Cluster breakdown = minPopulation();
            this.districts.remove(breakdown);
            breakCluster(breakdown);
        }
        int i = 0;
        for(Cluster c : this.districts){
            c.setClusterID(i++);
        }
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public Demographics getDemographics() {
        return demographics;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }
}
