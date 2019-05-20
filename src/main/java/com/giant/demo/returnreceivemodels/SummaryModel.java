package com.giant.demo.returnreceivemodels;

import java.util.List;

public class SummaryModel {
    private double scoreBeofore;
    private double scoreAfter;
    private PoliStats stateStats;
    private PoliStats actualStats;
    List<PoliStats> districtStat;

    public SummaryModel() {
    }

    public SummaryModel(double scoreBeofore, double scoreAfter, PoliStats stateStats, PoliStats actualStats, List<PoliStats> districtStat) {
        this.scoreBeofore = scoreBeofore;
        this.scoreAfter = scoreAfter;
        this.stateStats = stateStats;
        this.actualStats = actualStats;
        this.districtStat = districtStat;
    }

    public double getScoreBeofore() {
        return scoreBeofore;
    }

    public void setScoreBeofore(double scoreBeofore) {
        this.scoreBeofore = scoreBeofore;
    }

    public double getScoreAfter() {
        return scoreAfter;
    }

    public void setScoreAfter(double scoreAfter) {
        this.scoreAfter = scoreAfter;
    }

    public PoliStats getStateStats() {
        return stateStats;
    }

    public void setStateStats(PoliStats stateStats) {
        this.stateStats = stateStats;
    }

    public PoliStats getActualStats() {
        return actualStats;
    }

    public void setActualStats(PoliStats actualStats) {
        this.actualStats = actualStats;
    }

    public List<PoliStats> getDistrictStat() {
        return districtStat;
    }

    public void setDistrictStat(List<PoliStats> districtStat) {
        this.districtStat = districtStat;
    }
}
