package com.giant.demo.entities;

import com.giant.demo.enums.Race;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClusterEdge {


    private int edgeID;
    private Cluster cluster1;
    private Cluster cluster2;
    private double joinability;


    public ClusterEdge() {
    }

    public ClusterEdge(Cluster cluster1, Cluster cluster2) {
        this.cluster1 = cluster1;
        this.cluster2 = cluster2;
    }

    public boolean equals(ClusterEdge edge){
        return this.cluster2.getClusterID() == edge.cluster2.getClusterID();
    }

    //used for sorting
    public int compareTo(ClusterEdge e2){
        double score = e2.getJoinability();
        if(this.joinability == score)
            return 0;
        else if(this.joinability > score)
            return 1;
        return -1;
    }

    //calculate joinability based of percentages from demographics
    public double calculateJoinability(Job j){
        //calculated the percentage of representation for each race.
        double score = 0.0;
        //African American
        if(j.getAfricanAmerican() != null) {
            double aa = cluster1.getDemographics().getAfricanAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getAfricanAmerican() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
            score += aa;
        }
        //Asian
        if(j.getAsian() != null) {
            double a = cluster1.getDemographics().getAsian() * cluster1.getPopulation() + cluster2.getDemographics().getAsian() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
            score += a;
        }
        //Latin American
        if(j.getLatinAmerican() != null) {
            double la = cluster1.getDemographics().getLatinAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getLatinAmerican() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
            score += la;
        }
        Set<String> counties1 = cluster1.getCounties().keySet();
        Set<String> counties2 = cluster2.getCounties().keySet();

//        counties1.remove(null);
//        counties2.remove(null);
//        Set<String> counties3 = counties1;
//        counties3.retainAll(counties2);
//        if(counties2.size() > 0) {
//            counties1.addAll(counties2);
//        }

        Set<String> union = new HashSet<>();
        union.addAll(counties1);
        union.addAll(counties2);

        Set<String> c1 = new HashSet<>();
        Set<String> c2 = new HashSet<>();
        c1.addAll(counties1);
        c2.addAll(counties2);

        c1.retainAll(c2);
        score += 3*(c1.size()/(union.size() +1));
//        score += 3 * (counties3.size() / (counties1.size() + 1));
        return score;
    }

    public void calculateJoinability(){

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

    public double getJoinability(Job job) {
        this.setJoinability(calculateJoinability(job));
        return joinability;
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

    public String toString(){
        return "Edge: " + this.joinability;
    }
}
