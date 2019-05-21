package com.giant.demo.returnreceivemodels;

import com.giant.demo.entities.Cluster;
import com.giant.demo.entities.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SummaryModel {
    private double scoreBefore;
    private double scoreAfter;
    private PoliStats stateStats;
    private PoliStats actualStats;
    List<PoliStats> districtStat;

    public SummaryModel() {
    }

    public SummaryModel(State realState) {
        this.scoreBefore = -1;
        Set<Cluster> districts = realState.getDistricts();
        int numActualReps = 0;
        int numActualDems = 0;
        int votes = 0;
        int numStateReps = 0;
        int numStateDems = 0;
        this.districtStat = new ArrayList<>();
        for (Cluster d : districts) {
            int numDRep = d.getNumRep();
            int numDDem = d.getNumDemo();
            int numVotes = d.getVotes();
            numActualReps += numDRep;
            numActualDems += numDDem;
            votes += numVotes;
            districtStat.add(new PoliStats(numDDem, numDRep, -1, -1));
            if (numDDem > numDRep) {
                numStateDems += 1;
            } else if (numDDem < numDRep) {
                numStateReps += 1;
            }
        }
        double actualRepPercent = ((double)numActualReps) / votes;
        double actualDemPercent = ((double)numActualDems) / votes;
        this.actualStats = new PoliStats(numActualDems, numActualReps, actualDemPercent, actualRepPercent);
        double stateRepPercent = ((double)numStateReps) / districts.size();
        double stateDemPercent = ((double)numStateDems) / districts.size();
        this.stateStats = new PoliStats(numStateDems, numStateReps, stateDemPercent, stateRepPercent);

        double repDiff = Math.abs(actualRepPercent - stateRepPercent) * 100;
        repDiff = (repDiff == 0) ? 1 : repDiff;
        double demDiff = Math.abs(actualDemPercent - stateDemPercent) * 100;
        demDiff = (demDiff == 0) ? 1 : demDiff;
        this.scoreAfter = (0.5 / repDiff) + (0.5 / demDiff);
    }

    public SummaryModel(double scoreBefore, double scoreAfter, PoliStats stateStats, PoliStats actualStats, List<PoliStats> districtStat) {
        this.scoreBefore = scoreBefore;
        this.scoreAfter = scoreAfter;
        this.stateStats = stateStats;
        this.actualStats = actualStats;
        this.districtStat = districtStat;
    }

    public double getScoreBefore() {
        return scoreBefore;
    }

    public void setScoreBefore(double scoreBefore) {
        this.scoreBefore = scoreBefore;
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
