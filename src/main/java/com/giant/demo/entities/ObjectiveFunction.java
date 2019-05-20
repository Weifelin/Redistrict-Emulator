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
import org.locationtech.jts.geom.Polygon;
import sun.invoke.empty.Empty;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Float.NaN;

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
        this.minScores.put(Measures.EfficiencyGap, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.PolsbyPopper, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Reock, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.ConvexHull, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Schwartzberg, Double.POSITIVE_INFINITY);
        this.maxScores.put(Measures.Compactness, 0.0);
        this.maxScores.put(Measures.Partisan, 0.0);
        this.maxScores.put(Measures.Population, 0.0);
        this.maxScores.put(Measures.EfficiencyGap,0.0);
        this.maxScores.put(Measures.PolsbyPopper, 0.0);
        this.maxScores.put(Measures.Reock, 0.0);
        this.maxScores.put(Measures.ConvexHull, 0.0);
        this.maxScores.put(Measures.Schwartzberg, 0.0);
        this.state = state;
    }

    public double normalize(double x, double min, double max) {
        if(min == Double.POSITIVE_INFINITY || min == max){
            min = x * 0.5;
        }
        if(max == 0.0 || max == x){
            max = x * 1.2;
        }
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
        if(c.getBoundary().isEmpty()){
            return 0.0;
        }
        double polsbyPopper = 0, reock = 0, convexHull = 0, schwartzberg = 0;
        double maxP = 0, maxR = 0, maxC = 0, maxS = 0;
        double minP = 20, minR = 20, minC = 20, minS = 20;
        //Polsby-Popper
        //Area divided by Perimeter
        //double A = Math.PI * Math.pow(c.getBoundary().getLength() / (2 * Math.PI), 2);
        //polsbyPopper += 4 * Math.PI * (A / Math.pow(c.getBoundary().getLength(), 2));
        //Reock
        //Area of the Distrist divided by the area of the minimum bounding circle
        Geometry circle = new MinimumBoundingCircle(c.getBoundary()).getCircle();
        reock += c.getBoundary().getArea() / circle.getArea();
        //ConvexHull
        //ratio of area of district to area of convex hull
        Geometry polygon = new ConvexHull(c.getBoundary()).getConvexHull();
        convexHull += c.getBoundary().getArea() / polygon.getArea();
        //Schwartzberg
        //ratio of perimeter of the district to circumference of circle whose area is
        //equal to area of district
        double r = Math.sqrt(c.getBoundary().getArea() / Math.PI);
        schwartzberg += 1 / (c.getBoundary().getLength() / (2 * Math.PI * r));

        //log min and max
        if (polsbyPopper > maxScores.get(Measures.PolsbyPopper)) {
            maxScores.put(Measures.PolsbyPopper, polsbyPopper);
        }
        if (polsbyPopper < minScores.get(Measures.PolsbyPopper)) {
            minScores.put(Measures.PolsbyPopper, polsbyPopper);
        }
        polsbyPopper = normalize(polsbyPopper, minP, maxP);
        polsbyPopper *= compactnessWeights.getPolsPopSlider();
        //System.out.println("PolsbyyPopper: " + polsbyPopper);

        //reock = normalize(reock, minR, maxR);
        if (reock > maxScores.get(Measures.Reock)) {
            maxScores.put(Measures.Reock, reock);
        }
        if (reock < minScores.get(Measures.Reock)) {
            minScores.put(Measures.Reock, reock);
        }
        reock *= compactnessWeights.getReockSlider();
        //System.out.println("Reock: " + reock);

        //convexHull = normalize(convexHull, minC, maxC);
        if (convexHull > maxScores.get(Measures.ConvexHull)) {
            maxScores.put(Measures.ConvexHull, convexHull);
        }
        if (convexHull < minScores.get(Measures.ConvexHull)) {
            minScores.put(Measures.ConvexHull, convexHull);
        }
        convexHull *= compactnessWeights.getConvexHullSlider();
        //System.out.println("ConvexHull: " + convexHull);

        //schwartzberg = normalize(schwartzberg, minS, maxS);
        if (schwartzberg > maxScores.get(Measures.Schwartzberg)) {
            maxScores.put(Measures.Schwartzberg, schwartzberg);
        }
        if (schwartzberg < minScores.get(Measures.Schwartzberg)) {
            minScores.put(Measures.Schwartzberg, schwartzberg);
        }
        schwartzberg *= compactnessWeights.getSchwartzSlider();
        //System.out.println("Schwartsberg: " + schwartzberg);
        double score = reock + convexHull + schwartzberg;//+polysbypopper
        if (score > maxScores.get(Measures.Compactness)) {
            maxScores.put(Measures.Compactness, score);
        }
        if (score < minScores.get(Measures.Compactness)) {
            minScores.put(Measures.Compactness, score);
        }
        return score;
    }


    public double calculateParisanScore(Cluster c) {
        double numDP = 0.0, numRP = 0.0, numIP = 0.0;
        for(Precinct p : c.getContainedPrecincts()){
            if (c.getPartyPreference() == PartyPreference.BLUE)
                numDP++;
            else if (c.getPartyPreference() == PartyPreference.RED)
                numRP++;
            else
                numIP++;
        }
        double numPrecincts = c.getContainedPrecincts().size();
        numDP /= numPrecincts;
        numRP /= numPrecincts;
        numIP /= numPrecincts;
        double numVotes = c.getVotes();
        double numDV = c.getNumDemo() / numVotes;
        double numRV = c.getNumRep() / numVotes;
        double numIV = (numVotes - c.getNumDemo() - c.getNumRep()) / numVotes;
        double score = 1 / Math.abs(numDP - numDV) + 1 / Math.abs(numRP - numRV) + 1 / Math.abs(numIP - numIV);
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
        double score = Math.abs(rWaste - dWaste) / c.getContainedPrecincts().size();
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
        score = (avgPop == c.getPopulation()) ? 1 : 1 / Math.abs(avgPop - c.getPopulation());
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
        //System.out.println("Compactness: " + score);
        score += normalize(calculatePopulationScore(c), minScores.get(Measures.Population), maxScores.get(Measures.Population)) * populationEqualityWeight;
        //System.out.println("Pop score: " + score);
        score += normalize(calculateParisanScore(c), minScores.get(Measures.Partisan), maxScores.get(Measures.Partisan)) * partisanFairnessWeight;
        //System.out.println("Partisan: " + score);
        score += normalize(calculateEfficiencyGap(c), minScores.get(Measures.EfficiencyGap), maxScores.get(Measures.EfficiencyGap)) * efficiencyGapWeight;
        return score;
    }
}
