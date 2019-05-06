package com.giant.demo.entities;

import com.giant.demo.enums.StateE;
import javafx.util.Pair;

import javax.persistence.*;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int jobID;
    private double demographicsWeight;
    private double compactnessWeight;
    private double contiguityWeight;
    private double populationWeight;
    private double partisanWeight;
    @Transient
    private Pair<Double, Double> AArange;
    @Transient
    private Pair<Double, Double> Arange;
    @Transient
    private Pair<Double, Double> Wrange;
    @Transient
    private Pair<Double, Double> LArange;

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



    public double getPartisanWeight() {
        return partisanWeight;
    }

    public void setPartisanWeight(double partisanWeight) {
        this.partisanWeight = partisanWeight;
    }

    public Pair<Double, Double> getAArange() {
        return AArange;
    }

    public void setAArange(Pair<Double, Double> AArange) {
        this.AArange = AArange;
    }

    public Pair<Double, Double> getArange() {
        return Arange;
    }

    public void setArange(Pair<Double, Double> arange) {
        Arange = arange;
    }

    public Pair<Double, Double> getWrange() {
        return Wrange;
    }

    public void setWrange(Pair<Double, Double> wrange) {
        Wrange = wrange;
    }

    public Pair<Double, Double> getLArange() {
        return LArange;
    }

    public void setLArange(Pair<Double, Double> LArange) {
        this.LArange = LArange;
    }
}
