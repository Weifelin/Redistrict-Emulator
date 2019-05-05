package com.giant.demo.entities;

import com.giant.demo.enums.Measures;
import com.giant.demo.enums.PartyPreference;
import com.giant.demo.enums.Race;
import org.hibernate.mapping.Table;
import org.locationtech.jts.algorithm.ConvexHull;
import org.locationtech.jts.algorithm.MinimumBoundingCircle;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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
    private State state;
    private Map<Measures, Double> maxScores;
    private Map<Measures, Double> minScores;

    public ObjectiveFunction(State state) {
        this.state = state;
    }

    public ObjectiveFunction(double demographicScore, double compactnessScore, double contiguity, double partisanScore, double populationScore) {
        this.demographicScore = demographicScore;
        this.compactnessScore = compactnessScore;
        this.contiguity = contiguity;
        this.partisanScore = partisanScore;
        this.populationScore = populationScore;
        this.minScores.put(Measures.Demographics, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Compactness, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Contiguity, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Partisan, Double.POSITIVE_INFINITY);
        this.minScores.put(Measures.Population, Double.POSITIVE_INFINITY);
    }

    public double normalize(double x, double min, double max){
        return (x - min) / (max - min);
    }

    public void normalizedObjectiveFunction(){
        setDemographicScore(normalize(getDemographicScore(), minScores.get(Measures.Demographics), maxScores.get(Measures.Demographics)));
        setCompactnessScore(normalize(getCompactnessScore(), minScores.get(Measures.Compactness), maxScores.get(Measures.Compactness)));
        setContiguity(normalize(getContiguity(), minScores.get(Measures.Contiguity), maxScores.get(Measures.Contiguity)));
        setPopulationScore(normalize(getPopulationScore(), minScores.get(Measures.Population), maxScores.get(Measures.Population)));
        setPartisanScore(normalize(getPartisanScore(), minScores.get(Measures.Partisan), maxScores.get(Measures.Partisan)));
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
        int mmDistricts = 0;
        for(Cluster c : this.state.getDistricts()){
            Demographics demo = c.getDemographics();

            double demoScore = 0, temp;
            int count = 0;
            if(this.state.getDemographics().getRaces().contains(Race.African_American)) {
                temp = Math.abs(demo.getAfricanAmerican() - this.state.getDemographics().getAfricanAmerican());
                demoScore = (temp != 0) ? 1 / temp : 1;
                count++;
            }
            else if(this.state.getDemographics().getRaces().contains(Race.Asian)) {
                temp = Math.abs(demo.getAsian() - this.state.getDemographics().getAsian());
                demoScore = (temp != 0) ? 1 / temp : 1;
                count++;
            }
            else if(this.state.getDemographics().getRaces().contains(Race.White)) {
                temp = Math.abs(demo.getWhite() - this.state.getDemographics().getWhite());
                demoScore = (temp != 0) ? 1 / temp : 1;
                count++;
            }
            else if(this.state.getDemographics().getRaces().contains(Race.Latin_American)){
                temp = Math.abs(demo.getLatinAmerican() - this.state.getDemographics().getLatinAmerican());
                demoScore = (temp != 0) ? 1 / temp : 1;
                count++;
            }
            demoScore /= count;
            if(demoScore > maxScores.get(Measures.Demographics)){
                maxScores.put(Measures.Demographics, demoScore);
            }
            if(demoScore < minScores.get(Measures.Demographics)){
                minScores.put(Measures.Demographics, demoScore);
            }
            score += demoScore;
            if(demo.getWhite() < demo.getLatinAmerican() || demo.getWhite() < demo.getAsian() || demo.getWhite() < demo.getAfricanAmerican())
                mmDistricts++;
        }
        score /= this.state.getNumOfDistricts();
        setDemographicScore(Math.pow(score, Math.abs(mmDistricts - this.state.getNumMinorityDistricts())));
        return getDemographicScore();
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
        if(score > maxScores.get(Measures.Compactness)){
            maxScores.put(Measures.Compactness, score);
        }
        if(score < minScores.get(Measures.Compactness)){
            minScores.put(Measures.Compactness, score);
        }
        setCompactnessScore(score);
        return score;//normalize(score, 0, 4);
    }//makes sure to nomralize between 0 and 4 to makes sure that is isnt double normalized

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
        setPartisanScore(score);
        if(score > maxScores.get(Measures.Partisan)){
            maxScores.put(Measures.Partisan, score);
        }
        if(score < minScores.get(Measures.Partisan)){
            minScores.put(Measures.Partisan, score);
        }
        return score;
    }

    public double calculatePopulationScore(){
        double score = 0, avgPop = this.state.getPopulation() / this.state.getDistricts().size();
        for(Cluster c : this.state.getDistricts()){
            score += 1 / Math.abs(avgPop - c.getPopulation());
        }
        setPopulationScore(score);
        if(score > maxScores.get(Measures.Population)){
            maxScores.put(Measures.Population, score);
        }
        if(score < minScores.get(Measures.Population)){
            minScores.put(Measures.Population, score);
        }
        return score;
    }

    public double calculateContiguity() {
        double score = 0, min = Double.POSITIVE_INFINITY, max = 0;
        for (Cluster c : this.state.getDistricts()) {
            Geometry polygon = new ConvexHull(c.getBoundary()).getConvexHull();
            double convexHull = c.getBoundary().getArea() / polygon.getArea();
            max = (convexHull > max) ? convexHull : max;
            min = (convexHull < max) ? convexHull : min;
            score += convexHull;
        }
        score /= this.state.getNumOfDistricts();
        setContiguity(score);
        if(score > maxScores.get(Measures.Contiguity)){
            maxScores.put(Measures.Contiguity, score);
        }
        if(score < minScores.get(Measures.Contiguity)){
            minScores.put(Measures.Contiguity, score);
        }
        return score;//normalize(score, min, max);
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
