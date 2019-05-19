package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import com.giant.demo.enums.Race;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.*;

@Entity
public class Cluster {
    @Id
    private int clusterID;
    public int level;
    @OneToMany
    private List<Precinct> containedPrecincts;
    private Geometry boundary;
    @OneToOne
    private ObjectiveFunction objectiveFunction;
    /*@Transient
    private List<ClusterEdge> edges;*/
    private int population;
    @OneToOne
    private Demographics demographics;
    private PartyPreference partyPreference;

    @Transient
    private Map<String, Integer> counties;
    private boolean isMajorityMinority;
    private int numDemo;
    private int numRep;
    private int votes;
    /*@Transient
    private Map<Integer, ClusterEdge> clusterEdgeMap;*/
    @Transient
    private Set<String> edgeIDs;



    public Cluster(int clusterID) {
        this.clusterID = clusterID;
    }

    public Cluster(int clusterID, List<Precinct> containedPrecincts) {
        this.clusterID = clusterID;
        this.containedPrecincts = new ArrayList<>();
        this.containedPrecincts = containedPrecincts;
        Precinct p = containedPrecincts.get(0);
        this.population = p.getPopulation();
        this.demographics = p.getDemogrpahics();
        this.counties = new HashMap<>();
        this.counties.put(p.getCountyID(), 1);
        this.numDemo = p.getNumDemo();
        this.numRep = p.getNumRep();
        this.votes = p.getVotes();
        this.boundary = p.getBoundaries();
        this.level = 0;
        this.edgeIDs = new HashSet<>();
    }

    public void addPrecinct(Precinct p){
        //this.addPopulation(p.getPopulation());
        if(this.counties.containsKey(p.getCountyID())){
            int count = this.counties.get(p.getCountyID());
            this.counties.replace(p.getCountyID(), count + 1);
        }
        else{
            this.counties.put(p.getCountyID(), 1);
        }
        this.containedPrecincts.add(p);
        this.numDemo += p.getNumDemo();
        this.numRep += p.getNumRep();
        this.votes += p.getVotes();
        this.boundary.union(p.getBoundaries());
    }

    public void removePrecinct(Precinct precinct){
        if (containedPrecincts.contains(precinct)){
            this.population -= precinct.getPopulation();
            /*county operation*/
            if(counties.get(precinct.getCountyID()) > 1){
                counties.replace(precinct.getCountyID(), counties.get(precinct.getCountyID()) - 1);
            }
            else{
                counties.remove(precinct.getCountyID());
            }
            containedPrecincts.remove(precinct);
            numDemo -= precinct.getNumDemo();
            numRep -= precinct.getNumRep();
            votes -= precinct.getVotes();
            /*boundary operation*/
            boundary = boundary.difference(precinct.getBoundaries());

        }
    }




    public Cluster combineCluster(Cluster c2){
        for(Precinct precinct : c2.getContainedPrecincts()){
            this.addPrecinct(precinct);
        }
        //c2.setContainedPrecincts(null);
        this.demographics.combine(c2.getDemographics());
        return c2;
    }

    public int compareTo(Cluster c2){
        if(this.population == c2.getPopulation())
            return 0;
        return (this.population > c2.getPopulation()) ? -1 : 1;
    }

    public PartyPreference getPartyPreference() {
        return partyPreference;
    }

    public void setPartyPreference(PartyPreference partyPreference) {
        this.partyPreference = partyPreference;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public boolean isMajorityMinority(Cluster c) {
        double percent = 0.0;
        Set<Race> races = c.getDemographics().getRaces();
        if(races.contains(Race.African_American)){
            percent += c.getDemographics().getAfricanAmerican();
        }
        else if(races.contains(Race.Asian)){
            percent += c.getDemographics().getAsian();
        }
        else if(races.contains(Race.Latin_American)){
            percent += c.getDemographics().getLatinAmerican();
        }
        return percent >= 0.5;
    }

    public Precinct getPrecinct(int n){
        for(Precinct p : this.getContainedPrecincts()){
            if(p.getPrecinctID() == n)
                return p;
        }
        return null;
    }

    public String toString(){
        String ret = "ClusterID: " + clusterID +
                "\nPopulation:  " + population; //+
                //"\nEdges: " + edges;// +
                //"\nPrecincts: " + containedPrecincts;
        return ret;
    }


    public double rateDistrict() {
        return -1;
    }

    public Set<String> getEdgeIDs() {
        return edgeIDs;
    }

    public void setEdgeIDs(Set<String> edgeIDs) {
        this.edgeIDs = edgeIDs;
    }

    public void addEdgeID(String key){
        this.edgeIDs.add(key);
    }

    public boolean removeEdgeID(String key) {
        return this.edgeIDs.remove(key);
    }

    public boolean removeAllEdgeID(Set<String> keys) {
        return this.edgeIDs.removeAll(keys);
    }

    public boolean equalsC(Cluster c2) {
        return this.getClusterID() == c2.getClusterID();
    }

    @Override
    public boolean equals(Object obj) {
        return this.equalsC((Cluster) obj);
    }

    public boolean mergeInto(Cluster c){
        return false;
    }

    public void destory(){

    }

    public Map<String, Integer> getCounties() {
        return counties;
    }

    public void setCounties(Map<String, Integer> counties) {
        this.counties = counties;
    }

    public boolean updateBoundary(){
        return false;
    }

    public Demographics getDemographics(){ return demographics; }

    public Precinct getNextPrecinct(){
        return null;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public List<Precinct> getContainedPrecincts() {
        return containedPrecincts;
    }

    public void setContainedPrecincts(ArrayList<Precinct> containedPrecincts) {
        this.containedPrecincts = containedPrecincts;
    }

    public Geometry getBoundary() {
        return boundary;
    }

    public void setBoundary(Geometry boundary) {
        this.boundary = boundary;
    }

    public ObjectiveFunction getObjectiveFunction() {
        return objectiveFunction;
    }

    public void setObjectiveFunction(ObjectiveFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public int getPopulation(){
        return this.population;
    }
    public void setPopulation(int pop){ this.population = pop;}


    public void addPopulation(int pop){
        this.population += pop;
    }


    public void setContainedPrecincts(List<Precinct> containedPrecincts) {
        this.containedPrecincts = containedPrecincts;
    }

    public boolean isMajorityMinority() {
        return isMajorityMinority;
    }

    public void setMajorityMinority(boolean majorityMinority) {
        isMajorityMinority = majorityMinority;
    }

    public int getNumDemo() {
        return numDemo;
    }

    public void setNumDemo(int numDemo) {
        this.numDemo = numDemo;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public int getNumRep() {
        return numRep;
    }

    public void setNumRep(int numRep) {
        this.numRep = numRep;
    }



}
