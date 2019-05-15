package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
public class Cluster {
    @Id
    private int clusterID;
    public int level = 0;
    @OneToMany
    private List<Precinct> containedPrecincts;
    private Geometry boundary;
    @OneToOne
    private ObjectiveFunction objectiveFunction;
    @Transient
    private List<ClusterEdge> edges;
    private int population;
    @OneToOne
    private Demographics demographics;
    private PartyPreference partyPreference;
    private boolean isMajorityMinority;



    public Cluster(int clusterID) {
        this.clusterID = clusterID;
    }

    public Cluster(int clusterID, List<Precinct> containedPrecincts) {
        this.clusterID = clusterID;
        this.containedPrecincts = containedPrecincts;
        this.edges = new ArrayList<>();
        this.population = containedPrecincts.get(0).getPopulation();
    }

    public boolean mergeInto(Cluster c){
        return false;
    }

    public void destory(){

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

    public List<ClusterEdge> getEdges() {
        return edges;
    }

    public void addEdge(ClusterEdge edge){
        this.edges.add(edge);
    }

    public void setEdges(ArrayList<ClusterEdge> edges) {
        this.edges = edges;
    }

    public List<ClusterEdge> combineEdges(List<ClusterEdge> edges){
        for(ClusterEdge edge1 : this.edges){
            for(ClusterEdge edge2 : edges){
                if(edge1.equals(edge2))
                    edge1.setJoinability((edge1.getJoinability() + edge2.getJoinability()) / 2);
                else
                    this.addEdge(edge2);
            }
        }
        return this.edges;
    }

    public void addPopulation(int pop){
        this.population += pop;
    }

    public void addPrecinct(Precinct p){
        this.addPopulation(p.getPopulation());
        this.containedPrecincts.add(p);
    }

    public void sortEdges(){
        Collections.sort(this.edges, (e1, e2) -> e1.compareTo(e2));
    }

    public ClusterEdge findClusterPair(int numClusters, int totalPop){
        double max = 0.0;
        ClusterEdge bestEdge = null;
        double popUpperBound = totalPop / ((double)numClusters / 2.0);
        for(ClusterEdge e : this.edges){
            int combinePop = e.getCluster1().getPopulation() + e.getCluster2().getPopulation();
            if(combinePop <= popUpperBound && e.getJoinability() > max){
                max = e.getJoinability();
                bestEdge = e;
            }
        }
        this.sortEdges();
        return bestEdge;
    }

    public void combineCluster(Cluster c2){
        this.addPopulation(c2.getPopulation());
        this.containedPrecincts.addAll(c2.getContainedPrecincts());
        this.combineEdges(c2.getEdges());
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

    public boolean isMajorityMinority() {
        int pop = demographics.getPopulation();
        int white = pop - (int) (demographics.getLatinAmerican()*pop) - (int) (demographics.getAsian()*pop) - (int)(demographics.getAfricanAmerican()*pop);
        if(white < demographics.getLatinAmerican() || white < demographics.getAsian() || white < demographics.getAfricanAmerican())
            return true;
        return false;
    }

    public Precinct getPrecinct(int n){
        for(Precinct p : this.getContainedPrecincts()){
            if(p.getPrecinctID() == n)
                return p;
        }
        return null;
    }
}
