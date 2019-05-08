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
    private String clusterID;
    public int level = 0;
    @OneToMany
    private List<Precinct> containedPrecincts;
    private Geometry boundary;
    @OneToOne
    private ObjectiveFunction objectiveFunction;
    @Transient
    private ArrayList<ClusterEdge> edges;
    private int population;
    @OneToOne
    private Demographics demographics;
    private PartyPreference partyPreference;



    public Cluster(String clusterID) {
        this.clusterID = clusterID;
    }

    public Cluster(String clusterID, ArrayList<Precinct> containedPrecincts) {
        this.clusterID = clusterID;
        this.containedPrecincts = containedPrecincts;
    }

    //check to see if edges point to the same cluster, then merge
    public ArrayList<ClusterEdge> combineEdges(ArrayList<ClusterEdge> edges){
        for(ClusterEdge edge1 : this.edges){
            for(ClusterEdge edge2 : edges){
                if(edge1.equals(edge2))
                    edge1.mergeEdge(edge2);
                else
                    this.addEdge(edge2);
            }
        }
        return edges;
    }

    public void addPrecinct(Precinct p){
        this.addPopulation(p.getPopulation());
        this.containedPrecincts.add(p);
    }

    //sport edges based off joinability
    public void sortEdges(){
        Collections.sort(this.edges, (e1, e2) -> e1.compareTo(e2));
    }

    //checks if combined with neighbor falls within population then calculate joinability.
    public ClusterEdge findClusterPair(int numClusters, int totalPop){
        double max = 0.0;
        ClusterEdge bestEdge = null;
        int popUpperBound = totalPop / (numClusters / 2);
        for(ClusterEdge e : edges){
            int combinePop = e.getCluster1().getPopulation() + e.getCluster2().getPopulation();
            if(combinePop <= popUpperBound && e.getJoinability() > max){
                max = e.getJoinability();
                bestEdge = e;
            }
        }
        this.sortEdges();
        return bestEdge;
    }

    //combine edges consolidate edges
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

    public void addPopulation(int pop){
        this.population += pop;
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

    public String getClusterID() {
        return clusterID;
    }

    public void setClusterID(String clusterID) {
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

    public ArrayList<ClusterEdge> getEdges() {
        return edges;
    }

    public void addEdge(ClusterEdge edge){
        this.edges.add(edge);
    }

    public void setEdges(ArrayList<ClusterEdge> edges) {
        this.edges = edges;
    }
}
