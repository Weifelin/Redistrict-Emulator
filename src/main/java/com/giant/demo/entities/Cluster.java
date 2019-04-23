package com.giant.demo.entities;

import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.Set;

public class Cluster {
    private int clusterID;
    private Set<Precinct> containedPrecincts;
    private Geometry boundary;
    private ObjectiveFunction objectiveFunction;
    private ArrayList<ClusterEdge> edges;

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

    public ArrayList<ClusterEdge> combineEdges(ArrayList<ClusterEdge> edges){
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

    public ArrayList<ClusterEdge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<ClusterEdge> edges) {
        this.edges = edges;
    }
}
