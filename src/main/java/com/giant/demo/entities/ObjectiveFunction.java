package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;

@Entity
public class ObjectiveFunction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int obID;
    private double demographicScore;
    private double compactnessScore;
    private double contiguity;
    private double partisanScore;
    private double populationScore;
    private double score;
    @OneToOne
    @JoinColumn(name = "stateID")
    private State state;

    public ObjectiveFunction(State state) {
        this.state = state;
    }

    public ObjectiveFunction(double demographicScore, double compactnessScore, double contiguity, double partisanScore, double populationScore) {
        this.demographicScore = demographicScore;
        this.compactnessScore = compactnessScore;
        this.contiguity = contiguity;
        this.partisanScore = partisanScore;
        this.populationScore = populationScore;
    }

    public double normalize(double x, double min, double max){
        return (x - min) / (max - min);
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
        double min = Double.POSITIVE_INFINITY, max = 0;
        double score = 0;
        for(Cluster c : this.state.getDistricts()){
            Demographics demo = c.getDemographics();
            double demoScore = 1 / Math.abs(demo.getAfricanAmerican() - this.state.getDemographics().getAfricanAmerican());
            demoScore += 1 / Math.abs(demo.getAsian() - this.state.getDemographics().getAsian());
            demoScore += 1 / Math.abs(demo.getWhite() - this.state.getDemographics().getWhite());
            demoScore += 1 / Math.abs(demo.getLatinAmerican() - this.state.getDemographics().getLatinAmerican());
            max = (demoScore > max) ? demoScore : max;
            min = (demoScore < min) ? demoScore : min;
            score += demoScore;
        }
        score /= this.state.getNumOfDistricts();
        return normalize(score, min, max);
    }

    public double calculateCompactnessScore(){
        double polsbyPopper = 0, reock = 0, convexHull = 0, schwartzberg = 0;
        double maxP = 0, maxR = 0, maxC = 0, maxS = 0;
        double minP = 20, minR = 20, minC = 20, minS = 20;
        for(Cluster c : this.state.getDistricts()) {
            //Polsby-Popper
            double A = Math.PI * Math.pow(c.getBoundary().getLength() / (2 * Math.PI), 2);
            polsbyPopper += 4 * Math.PI * (A / Math.pow(c.getBoundary().getLength(), 2));
            maxP = (polsbyPopper > maxP) ? polsbyPopper : maxP;
            minP = (polsbyPopper < minP) ? polsbyPopper : minP;
            //Reock
            Geometry circle = new MinimumBoundingCircle(c.getBoundary()).getCircle();
            reock += c.getBoundary().getArea() / circle.getArea();
            maxR = (reock > maxR) ? reock : maxR;
            minR = (reock < minR) ? reock : minR;
            //ConvexHull
            Geometry polygon = new ConvexHull(c.getBoundary()).getConvexHull();
            convexHull += c.getBoundary().getArea() / polygon.getArea();
            maxC = (convexHull > maxC) ? convexHull : maxC;
            minC = (convexHull < maxC) ? convexHull : minC;
            //Schwartzberg
            double r = Math.sqrt(c.getBoundary().getArea() / Math.PI);
            schwartzberg += 1 / (c.getBoundary().getLength() / (2 * Math.PI * r));
            maxS = (schwartzberg > maxS) ? schwartzberg : maxS;
            minS = (schwartzberg < minS) ? schwartzberg : minS;
        }
        double score = 0;
        polsbyPopper /= this.state.getNumOfDistricts();
        score += normalize(polsbyPopper, minP, maxP);
        reock /= this.state.getNumOfDistricts();
        score += normalize(reock, minR, maxR);
        convexHull /= this.state.getNumOfDistricts();
        score += normalize(convexHull, minC, maxC);
        schwartzberg /= this.state.getNumOfDistricts();
        score += normalize(schwartzberg, minS, maxS);
        return normalize(score, 0, 4);
    }

    public double calculateParisanScore(){
        double perDemo = 0, perRep = 0, perInd = 0;
        int numD = 0, numR = 0, numI = 0;
        for(Cluster c : this.state.getDistricts()){
            int numDemo = 0, numRep = 0, numInd = 0;
            for(Precinct p : c.getContainedPrecincts()){
                if(p.getPartyPreference() == PartyPreference.BLUE)
                    numDemo++;
                else if(p.getPartyPreference() == PartyPreference.RED)
                    numRep++;
                else
                    numInd++;
            }
            perDemo += numDemo / c.getContainedPrecincts().size();
            perRep += numRep / c.getContainedPrecincts().size();
            perInd += numInd / c.getContainedPrecincts().size();
            if(c.getPartyPreference() == PartyPreference.BLUE)
                numD++;
            else if(c.getPartyPreference() == PartyPreference.RED)
                numR++;
            else
                numI++;
        }
        double score = 0.0;
        score += 1 / Math.abs(perDemo - numD);
        score += 1 / Math.abs(perRep - numR);
        score += 1 / Math.abs(perInd - numI);
        return score;
    }

    public double calculatePopulationScore(){
        double score = 0, avgPop = this.state.getPopulation() / this.state.getDistricts().size();
        for(Cluster c : this.state.getDistricts()){
            score += 1 / Math.abs(avgPop - c.getPopulation());
        }
        return score;
    }

    public double calculateContiguity(){
        double score = 0, min = Double.POSITIVE_INFINITY, max = 0;
        for(Cluster c : this.state.getDistricts()){
            Geometry polygon = new ConvexHull(c.getBoundary()).getConvexHull();
            double convexHull = c.getBoundary().getArea() / polygon.getArea();
            max = (convexHull > max) ? convexHull : max;
            min = (convexHull < max) ? convexHull : min;
            score += convexHull;
        }
        score /= this.state.getNumOfDistricts();
        return normalize(score, min, max);
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

    public int getObID() {
        return obID;
    }
}
