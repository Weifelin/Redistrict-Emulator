package com.giant.demo.entities;

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
    public double calculateJoinability(Demographics d){
        //calculated the percentage of representation for each race.
        double score = 0.0;
        //African American
        double aa = cluster1.getDemographics().getAfricanAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getAfricanAmerican() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
        score += 1 / (aa - d.getAfricanAmerican());
        //White
        double w = cluster1.getDemographics().getWhite() * cluster1.getPopulation() + cluster2.getDemographics().getWhite() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
        score += 1 / (w - d.getWhite());
        //Asian
        double a = cluster1.getDemographics().getAsian() * cluster1.getPopulation() + cluster2.getDemographics().getAsian() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
        score += 1 / (a - d.getAsian());
        //Latin American
        double la = cluster1.getDemographics().getLatinAmerican() * cluster1.getPopulation() + cluster2.getDemographics().getLatinAmerican() * cluster2.getPopulation() / (cluster1.getPopulation() + cluster2.getPopulation());
        score += 1 / (la - d.getLatinAmerican());
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
}
