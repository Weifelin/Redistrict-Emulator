package com.giant.demo.entities;

import com.giant.demo.enums.Race;
import org.locationtech.jts.geom.Point;

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

    public double euclideanDistance(Precinct p1, Precinct p2){
        Point centroid1 = p1.getBoundaries().getCentroid();
        Point centroid2 = p2.getBoundaries().getCentroid();
        return Math.sqrt(Math.pow(centroid1.getX() - centroid2.getX(), 2) + Math.pow(centroid1.getY() - centroid2.getY(), 2));

    }

    public double compacted(){
        int c1Size = cluster1.getContainedPrecincts().size();
        int c2Size = cluster2.getContainedPrecincts().size();
        int index1 = (int)(Math.random() * c1Size);
        int index2 = (int)(Math.random() * c2Size);
        Precinct p1 = cluster1.getContainedPrecincts().get(index1);
        Precinct p2 = cluster2.getContainedPrecincts().get(index2);
        double score1 = 0.0;
        double score2 = 0.0;
        for(Precinct p : cluster2.getContainedPrecincts()){
            score1 += 1 / euclideanDistance(p1, p);
        }
        for(Precinct p : cluster1.getContainedPrecincts()){
            score2 += euclideanDistance(p2, p);
        }
        return (score1 + score2) / 2;
    }

    //calculate joinability based of percentages from demographics
    public double calculateJoinability(Job j){
        //calculated the percentage of representation for each race.
        double score = 0.0;
        //African American
        if(j.getAfricanAmerican() != null) {
            double aa = (cluster1.getDemographics().getAfricanAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getAfricanAmerican() * cluster2.getPopulation()) / (cluster1.getPopulation() + cluster2.getPopulation());
            score += aa;
        }
        //Asian
        if(j.getAsian() != null) {
            double a = (cluster1.getDemographics().getAsian() * cluster1.getPopulation() + cluster2.getDemographics().getAsian() * cluster2.getPopulation()) / (cluster1.getPopulation() + cluster2.getPopulation());
            score += a;
        }
        //Latin American
        if(j.getLatinAmerican() != null) {
            double la = (cluster1.getDemographics().getLatinAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getLatinAmerican() * cluster2.getPopulation()) / (cluster1.getPopulation() + cluster2.getPopulation());
            score += la;
        }
        Set<String> counties1 = cluster1.getCounties().keySet();
        Set<String> counties2 = cluster2.getCounties().keySet();

        Set<String> union = new HashSet<>();
        union.addAll(counties1);
        union.addAll(counties2);

        Set<String> c1 = new HashSet<>(counties1);
        Set<String> c2 = new HashSet<>(counties2);

        c1.retainAll(c2);
        score += 3*((double)(c1.size()+1)/(double)(union.size() +1));
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

    public double getJoinability(Job job, double goal, int pop, int numClusters) {
        double score = calculateJoinability(job);
        double popScore = (1 / (Math.abs(goal - (double)pop) / goal));// * 1000000000);
        double compact = compacted() / 100 * Math.log(numClusters);
        this.setJoinability(score + popScore + compact);
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
