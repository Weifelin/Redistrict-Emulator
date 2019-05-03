package com.giant.demo.entities;

import com.giant.demo.enums.StateE;

public class Job {
    private StateE state;
    private double demographicsWeight;
    private double compactnessWeight;
    private double contiguityWeight;
    private double populationWeight;
    private double partisanWeight;

    public Job(double demo, double comp, double cont, double pop, double party){
        this.demographicsWeight = demo;
        this.compactnessWeight = comp;
        this.contiguityWeight = cont;
        this.populationWeight = pop;
        this.partisanWeight = party;
    }

    public double getDemographicsWeight() {
        return demographicsWeight;
    }

    public void setDemographicsWeight(double demographicsWeight) {
        this.demographicsWeight = demographicsWeight;
    }

    public double getCompactnessWeight() {
        return compactnessWeight;
    }

    public void setCompactnessWeight(double compactnessWeight) {
        this.compactnessWeight = compactnessWeight;
    }

    public double getContiguityWeight() {
        return contiguityWeight;
    }

    public void setContiguityWeight(double contiguityWeight) {
        this.contiguityWeight = contiguityWeight;
    }

    public double getPopulationWeight() {
        return populationWeight;
    }

    public void setPopulationWeight(double populationWeight) {
        this.populationWeight = populationWeight;
    }

    public StateE getState() {
        return state;
    }

    public void setState(StateE state) {
        this.state = state;
    }

    public double getPartisanWeight() {
        return partisanWeight;
    }

    public void setPartisanWeight(double partisanWeight) {
        this.partisanWeight = partisanWeight;
    }
}
