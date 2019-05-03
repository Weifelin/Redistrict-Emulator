package com.giant.demo.entities;

public class ObjectiveFunction {
    private double demographicScore;
    private double compactnessScore;
    private double contiguity;
    private double partisanScore;
    private double populationScore;
    private double score;

    public ObjectiveFunction() {
    }

    public ObjectiveFunction(double demographicScore, double compactnessScore, double contiguity, double partisanScore, double populationScore) {
        this.demographicScore = demographicScore;
        this.compactnessScore = compactnessScore;
        this.contiguity = contiguity;
        this.partisanScore = partisanScore;
        this.populationScore = populationScore;
    }

    public double getDemographicScore() {
        return demographicScore;
    }



    public void setDemographicScore(double demographicScore) {
        this.demographicScore = demographicScore;
    }

    public double getCompactnessScore() {
        return compactnessScore;
    }

    public void setCompactnessScore(double compactnessScore) {
        this.compactnessScore = compactnessScore;
    }

    public double getContiguity() {
        return contiguity;
    }

    public void setContiguity(double contiguity) {
        this.contiguity = contiguity;
    }

    public double getPartisanScore() {
        return partisanScore;
    }

    public void setPartisanScore(double partisanScore) {
        this.partisanScore = partisanScore;
    }

    public double getPopulationScore() {
        return populationScore;
    }

    public void setPopulationScore(double populationScore) {
        this.populationScore = populationScore;
    }

    public double calculateDemographicScore(){
        return 0.0;
    }

    public double calculateCompactnessScore(){
        return 0.0;
    }

    public double calculateParisanScore(){
        return 0.0;
    }

    public double calculatePopulationScore(){
        return 0.0;
    }

    public double calculateContiguity(){
        return 0.0;
    }

    public double getScore(Job j) {
        int score = 0;
        score += calculateCompactnessScore() * j.getCompactnessWeight();
        score += calculateDemographicScore() * j.getDemographicsWeight();
        score += calculateParisanScore() * j.getPartisanWeight();
        score += calculatePopulationScore() * j.getPopulationWeight();
        score += calculateContiguity() * j.getContiguityWeight();
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
