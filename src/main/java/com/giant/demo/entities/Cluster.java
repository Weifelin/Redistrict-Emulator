package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

@Entity
public class Cluster {
    @Id
    private int clusterID;
    public int level = 0;
    @OneToMany
    private Set<Precinct> containedPrecincts;
    private Geometry boundary;
    @OneToOne
    private ObjectiveFunction objectiveFunction;
    @Transient
    private ArrayList<ClusterEdge> edges;
    private int population;
    @OneToOne
    private Demographics demographics;
    private PartyPreference partyPreference;



    public Cluster() {
    }

    public Cluster(int clusterID) {
        this.clusterID = clusterID;
    }

    public Cluster(int clusterID, Set<Precinct> containedPrecincts) {
        this.clusterID = clusterID;
        this.containedPrecincts = containedPrecincts;
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

    public Set<Precinct> getContainedPrecincts() {
        return containedPrecincts;
    }

    public void setContainedPrecincts(Set<Precinct> containedPrecincts) {
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
}
