package com.giant.demo.entities;

import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import com.giant.demo.jobObjects.AfricanAmerican;
import com.giant.demo.jobObjects.Asian;
import com.giant.demo.jobObjects.Compactness;
import com.giant.demo.jobObjects.LatinAmerican;
import javafx.util.Pair;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

public class Job {


    private Compactness compactnessWeight;
    private int numDistricts;
    private double partyFairSlier;
    private double efficiencyGapSlider;
    private double PopulationEquality;
    private int numMajorityMinorityDistricts;
    private AfricanAmerican africanAmerican;
    private LatinAmerican latinAmerican;
    private Asian asian;

    public Job() {
    }

    public Job(Compactness compactnessWeight, int numDistricts, double partyFairSlier, double efficiencyGapSlider, double populationEquality, int numMajorityMinorityDistricts, AfricanAmerican africanAmerican, LatinAmerican latinAmerican, Asian asian) {
        this.compactnessWeight = compactnessWeight;
        this.numDistricts = numDistricts;
        this.partyFairSlier = partyFairSlier;
        this.efficiencyGapSlider = efficiencyGapSlider;
        PopulationEquality = populationEquality;
        this.numMajorityMinorityDistricts = numMajorityMinorityDistricts;
        this.africanAmerican = africanAmerican;
        this.latinAmerican = latinAmerican;
        this.asian = asian;
    }

    public Compactness getCompactnessWeight() {
        return compactnessWeight;
    }

    public void setCompactnessWeight(Compactness compactnessWeight) {
        this.compactnessWeight = compactnessWeight;
    }

    public double getPartyFairSlier() {
        return partyFairSlier;
    }

    public void setPartyFairSlier(double partyFairSlier) {
        this.partyFairSlier = partyFairSlier;
    }

    public double getEfficiencyGapSlider() {
        return efficiencyGapSlider;
    }

    public void setEfficiencyGapSlider(double efficiencyGapSlider) {
        this.efficiencyGapSlider = efficiencyGapSlider;
    }

    public double getPopulationEquality() {
        return PopulationEquality;
    }

    public void setPopulationEquality(double populationEquality) {
        PopulationEquality = populationEquality;
    }

    public int getNumMajorityMinorityDistricts() {
        return numMajorityMinorityDistricts;
    }

    public void setNumMajorityMinorityDistricts(int numMajorityMinorityDistricts) {
        this.numMajorityMinorityDistricts = numMajorityMinorityDistricts;
    }

    public AfricanAmerican getAfricanAmerican() {
        return africanAmerican;
    }

    public void setAfricanAmerican(AfricanAmerican africanAmerican) {
        this.africanAmerican = africanAmerican;
    }

    public LatinAmerican getLatinAmerican() {
        return latinAmerican;
    }

    public void setLatinAmerican(LatinAmerican latinAmerican) {
        this.latinAmerican = latinAmerican;
    }

    public Asian getAsian() {
        return asian;
    }

    public void setAsian(Asian asian) {
        this.asian = asian;
    }

    public int getNumDistricts() {
        return numDistricts;
    }

    public void setNumDistricts(int numDistricts) {
        this.numDistricts = numDistricts;
    }
}
