package com.giant.demo.entities;

import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

public class Cluster {
    private int clusterID;
    private Set<Precinct> containedPrecincts;
    private Geometry boundary;
    private ObjectiveFunction objectiveFunction;
    private ArrayList<ClusterEdge> edges;
    private int population;

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

    public void sortEdges(){
        Collections.sort(this.edges, (e1, e2) -> e1.compareTo(e2));
    }

    public Cluster[] findClusterPair(int numClusters, int totalPop){
        double max = 0.0;
        Cluster candidate = null;
        int popUpperBound = totalPop / (numClusters / 2);
        for(ClusterEdge e : edges){
            int combinePop = e.getCluster1().getPopulation() + e.getCluster2().getPopulation();
            if(combinePop <= popUpperBound && e.getJoinability() > max){
                max = e.getJoinability();
                candidate = e.getCluster2();
            }
        }
        this.sortEdges();
        return (candidate != null) ? new Cluster[]{this.edges.get(0).getCluster1(), this.edges.get(0).getCluster2()} : null;
    }
}
