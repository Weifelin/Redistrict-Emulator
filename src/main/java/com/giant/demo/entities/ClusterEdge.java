package com.giant.demo.entities;

public class ClusterEdge {
    private Cluster cluster1;
    private Cluster cluster2;
    private double joinability;
    private int edgeID;

    public ClusterEdge() {
    }

    public ClusterEdge(Cluster cluster1, Cluster cluster2) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
    }

    public void calculateJoinability(){

    }

    public ClusterEdge mergeEdge(ClusterEdge c){
        return null;
    }



    public Cluster getCluster1() {
        return cluster1;
    }

    public void setCluster1(Cluster cluster1) {
        this.cluster1 = cluster1;
    }

    public Cluster getCluster2() {
        return cluster2;
    }

    public void setCluster2(Cluster cluster2) {
        this.cluster2 = cluster2;
    }

    public double getJoinability() {
        return joinability;
    }

    public void setJoinability(double joinability) {
        this.joinability = joinability;
    }

    public int getEdgeID() {
        return edgeID;
    }

    public void setEdgeID(int edgeID) {
        this.edgeID = edgeID;
    }

    public int compareTo(ClusterEdge e2){
        double score = e2.getJoinability();
        if(this.joinability == score)
            return 0;
        else if(this.joinability > score)
            return 1;
        return -1;
    }
}
