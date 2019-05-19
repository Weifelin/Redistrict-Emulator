package com.giant.demo.entities;

import com.giant.demo.enums.Measures;
import com.giant.demo.enums.PartyPreference;
import com.giant.demo.enums.Race;
import com.giant.demo.jobObjects.AfricanAmerican;
import com.giant.demo.jobObjects.Asian;
import com.giant.demo.jobObjects.Compactness;
import com.giant.demo.jobObjects.LatinAmerican;
import javafx.util.Pair;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class ObjectiveFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int obID;
    @Transient
    private Compactness compactnessWeights;
    private double partisanFairnessWeight;
    private double efficiencyGapWeight;
    private double populationEqualityWeight;
    private int numMinorityDistricts;
    @Transient
    private AfricanAmerican africanAmerican;
    @Transient
    private Asian asian;
    @Transient
    private LatinAmerican latinAmerican;
    @OneToOne
    @JoinColumn(name = "stateID")
    private State state;
    private double score;
    @Transient
    private Map<Measures, Double> maxScores;//max normalize scores
    @Transient
    private Map<Measures, Double> minScores;//min normalize scores

    public ObjectiveFunction(Job job, State state) {
        this.compactnessWeights = job.getCompactnessWeight();
        this.partisanFairnessWeight = job.getPartyFairSlier();
        this.efficiencyGapWeight = job.getEfficiencyGapSlider();
        this.populationEqualityWeight = job.getPopulationEquality();
        this.numMinorityDistricts = job.getNumDistricts();
        this.africanAmerican = job.getAfricanAmerican();
        this.asian = job.getAsian();
        this.latinAmerican = job.getLatinAmerican();
        this.minScores = new HashMap<>();
        this.maxScores = new HashMap<>();
        this.minScores.put(Measures.Compactness, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Partisan, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Population, Double.POSITIVE_INFINITY);
        this.state = state;
    }

    public double normalize(double x, double min, double max) {
        return (x - min) / (max - min);
    }

    //calculate the distance of x from the min and mx values
    public double distance(double x, Pair<Double, Double> pair) {
        double min = pair.getKey();
        double max = pair.getValue();
        if (x <= min)
            return x - min;
        else if (x >= max)
            return max - x;
        return 1;
    }

    //For each of the selected demographics the avg score per clusters is calculated
    public double calculateDemographicScore() {/*
        double min = Double.POSITIVE_INFINITY, max = 0;
        double score = 0;
        int mmDistricts = 0;
        for (Cluster c : this.state.getDistricts()) {
            Demographics demo = c.getDemographics();

            double demoScore = 0;
            int count = 0;
            if (this.state.getDemographics().getRaces().contains(Race.African_American)) {
                //demoScore = 1 / distance(demo.getAfricanAmerican(), this.state.getWeights().getAArange());
                count++;
            } else if (this.state.getDemographics().getRaces().contains(Race.Asian)) {
                //demoScore = 1 / distance(demo.getAsian(), this.state.getWeights().getArange());
                count++;
            } else if (this.state.getDemographics().getRaces().contains(Race.Latin_American)) {
                //demoScore = 1 / distance(demo.getLatinAmerican(), this.state.getWeights().getLArange());
                count++;
            }
            demoScore /= count;
            if (demoScore > maxScores.get(Measures.Demographics)) {
                maxScores.put(Measures.Demographics, demoScore);
            }
            if (demoScore < minScores.get(Measures.Demographics)) {
                minScores.put(Measures.Demographics, demoScore);
            }
            score += demoScore;
            int pop = demo.getPopulation();
            int white = pop - (int) (demo.getLatinAmerican() * pop) - (int) (demo.getAsian() * pop) - (int) (demo.getAfricanAmerican() * pop);
            if (white < demo.getLatinAmerican() || white < demo.getAsian() || white < demo.getAfricanAmerican())
                mmDistricts++;
        }

        score = Math.pow(score, Math.abs(mmDistricts - this.state.getNumMinorityDistricts()));
        return score;

        score = Math.pow(score, Math.abs(mmDistricts - this.state.getNumMinorityDistricts())));
        return score;*/
        return 0.0;

    }

    public double calculateCompactnessScore(Cluster c) {
        double polsbyPopper = 0, reock = 0, convexHull = 0, schwartzberg = 0;
        double maxP = 0, maxR = 0, maxC = 0, maxS = 0;
        double minP = 20, minR = 20, minC = 20, minS = 20;
        //Polsby-Popper
        //Area divided by Perimeter
        double A = Math.PI * Math.pow(c.getBoundary().getLength() / (2 * Math.PI), 2);
        polsbyPopper += 4 * Math.PI * (A / Math.pow(c.getBoundary().getLength(), 2));
        maxP = (polsbyPopper > maxP) ? polsbyPopper : maxP;
        minP = (polsbyPopper < minP) ? polsbyPopper : minP;
        //Reock
        //Area of the Distrist divided by the area of the minimum bounding circle
        Geometry circle = new MinimumBoundingCircle(c.getBoundary()).getCircle();
        reock += c.getBoundary().getArea() / circle.getArea();
        maxR = (reock > maxR) ? reock : maxR;
        minR = (reock < minR) ? reock : minR;
        //ConvexHull
        //ratio of area of district to area of convex hull
        Geometry polygon = new ConvexHull(c.getBoundary()).getConvexHull();
        convexHull += c.getBoundary().getArea() / polygon.getArea();
        maxC = (convexHull > maxC) ? convexHull : maxC;
        minC = (convexHull < maxC) ? convexHull : minC;
        //Schwartzberg
        //ratio of perimeter of the district to circumference of circle whose area is
        //equal to area of district
        double r = Math.sqrt(c.getBoundary().getArea() / Math.PI);
        schwartzberg += 1 / (c.getBoundary().getLength() / (2 * Math.PI * r));
        maxS = (schwartzberg > maxS) ? schwartzberg : maxS;
        minS = (schwartzberg < minS) ? schwartzberg : minS;
        polsbyPopper = normalize(polsbyPopper, minP, maxP);
        polsbyPopper *= compactnessWeights.getPolsPopSlider();
        reock = normalize(reock, minR, maxR);
        reock *= compactnessWeights.getReockSlider();
        convexHull = normalize(convexHull, minC, maxC);
        convexHull *= compactnessWeights.getConvexHullSlider();
        schwartzberg = normalize(schwartzberg, minS, maxS);
        schwartzberg *= compactnessWeights.getSchwartzSlider();
        double score = polsbyPopper + reock + convexHull + schwartzberg;
        if (score > maxScores.get(Measures.Compactness)) {
            maxScores.put(Measures.Compactness, score);
        }
        if (score < minScores.get(Measures.Compactness)) {
            minScores.put(Measures.Compactness, score);
        }
        return score;
    }


    public double calculateParisanScore() {
        double perDemo = 0, perRep = 0, perInd = 0;
        int numD = 0, numR = 0, numI = 0;
        for (Cluster c : this.state.getDistricts()) {
            int numDemo = 0, numRep = 0, numInd = 0;
            //percentage representation per cluster
            for (Precinct p : c.getContainedPrecincts()) {
                if (p.getPartyPreference() == PartyPreference.BLUE)
                    numDemo++;
                else if (p.getPartyPreference() == PartyPreference.RED)
                    numRep++;
                else
                    numInd++;
            }
            //percentage party affiliation
            perDemo += numDemo / c.getContainedPrecincts().size();
            perRep += numRep / c.getContainedPrecincts().size();
            perInd += numInd / c.getContainedPrecincts().size();
            if (c.getPartyPreference() == PartyPreference.BLUE)
                numD++;
            else if (c.getPartyPreference() == PartyPreference.RED)
                numR++;
            else
                numI++;
        }
        double score = 0.0;
        //measure distance of overall representation to each cluster
        score += 1 / Math.abs(perDemo - numD);
        score += 1 / Math.abs(perRep - numR);
        score += 1 / Math.abs(perInd - numI);
        if (score > maxScores.get(Measures.Partisan)) {
            maxScores.put(Measures.Partisan, score);
        }
        if (score < minScores.get(Measures.Partisan)) {
            minScores.put(Measures.Partisan, score);
        }
        return score;
    }

    public double calculateEfficiencyGap(Cluster c) {
        int numDemo = 0, numRep = 0;
        double rWaste, dWaste;
        int numDLoss = 0, numDExcess = 0, numRLoss = 0, numRExcess = 0;
        for (Precinct p : c.getContainedPrecincts()) {
            numDemo += p.getNumDemo();
            numRep += p.getNumDemo();
        }
        if (numDemo > numRep) {
            numDExcess += Math.floorDiv(numDemo - numRep, 2);
            numRLoss += numRep;
        } else {
            numRExcess += Math.floorDiv(numRep - numDemo, 2);
            numDLoss += numDemo;
        }
        rWaste = numRExcess + numRLoss;
        dWaste = numDExcess + numDLoss;
        double score = (numDemo > numRep) ? dWaste / numDemo : rWaste / numRep;
        if (score > maxScores.get(Measures.EfficiencyGap)) {
            maxScores.put(Measures.EfficiencyGap, score);
        }
        if (score < minScores.get(Measures.EfficiencyGap)) {
            minScores.put(Measures.EfficiencyGap, score);
        }
        return score;
    }

    //measure how close the population of each cluster is so being #people / # districts
    public double calculatePopulationScore(Cluster c) {
        double score, avgPop = this.state.getPopulation() / this.state.getDistricts().size();
        score = 1 / Math.abs(avgPop - c.getPopulation());
        if (score > maxScores.get(Measures.Population)) {
            maxScores.put(Measures.Population, score);
        }
        if (score < minScores.get(Measures.Population)) {
            minScores.put(Measures.Population, score);
        }
        return score;
    }

    public double getScore(Cluster c) {
        double score = 0.0;
        score += normalize(calculateCompactnessScore(c), minScores.get(Measures.Compactness), maxScores.get(Measures.Compactness));
        score += normalize(calculatePopulationScore(c), minScores.get(Measures.Population), maxScores.get(Measures.Population)) * populationEqualityWeight;
        score += normalize(calculateParisanScore(), minScores.get(Measures.Partisan), maxScores.get(Measures.Partisan)) * partisanFairnessWeight;
        score += normalize(calculateEfficiencyGap(c), minScores.get(Measures.EfficiencyGap), maxScores.get(Measures.EfficiencyGap)) * efficiencyGapWeight;
        return score;
    }
}
