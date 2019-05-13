package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import javax.servlet.http.Part;
import java.util.Set;

@Entity
public class Precinct {
    @Id
    private int precinctID;
    private StateE state;
    private int population;
    private Race majority;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private Geometry boundaries;
    @ManyToMany
    @JoinColumn(name = "precinctID")
    private Set<Precinct> neighbours;
    private PartyPreference partyPreference;
    private int numDemo;
    private int numRep;
    private int votes;
    @Transient
    private int[] tempNs;
    private String name;
    @OneToOne
    private Demographics demogrpahics;


    public Precinct() {
    }

    public Precinct(int precinctID, String name, int pop, int votes, double demo, double rep, Geometry polygon, Demographics demographics, StateE state, int[] tempNs){
        this.precinctID = precinctID;
        this.name = name;
        this.population = pop;
        this.votes = votes;
        this.numDemo = (int) demo;
        this.numRep = (int) rep;
        this.boundaries = polygon;
        this.demogrpahics = demographics;
        this.state = state;
        this.tempNs = tempNs;
        //Find Party Preference
        PartyPreference primary = (numDemo > numRep) ? PartyPreference.BLUE : PartyPreference.RED;
        int best = (primary == PartyPreference.BLUE) ? numDemo : numRep;
        this.partyPreference = (best >= (votes - numRep - numDemo)) ? primary : PartyPreference.GREEN;
        //Find Majority Race
        Race maj = Race.White;
        double race = (1.0 - demographics.getAsian() - demographics.getAfricanAmerican() - demographics.getLatinAmerican()) * 0.95;
        if (demographics.getAsian() >= race) {
            maj = Race.Asian;
            race = demographics.getAsian();
        }
        else if (demographics.getAfricanAmerican() >= race) {
            maj = Race.African_American;
            race = demographics.getAfricanAmerican();
        }
        else if (demographics.getLatinAmerican() >= race) {
            maj = Race.Latin_American;
        }
        this.majority = maj;

    }

    public Precinct(StateE state, int population, Race majority, Geometry boundaries) {

        this.state = state;
        this.population = population;
        this.majority = majority;
        this.boundaries = boundaries;
    }

    public int getPrecinctID() {
        return precinctID;
    }

    public StateE getState() {
        return state;
    }

    public void setState(StateE state) {
        this.state = state;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public Race getMajority() {
        return majority;
    }

    public void setMajority(Race majority) {
        this.majority = majority;
    }


    public Geometry getBoundaries() {
        return boundaries;
    }

    public Set<Precinct> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Precinct> neighbours) {
        this.neighbours = neighbours;
    }

    public void setBoundaries(Geometry boundaries) {
        this.boundaries = boundaries;
    }

    public PartyPreference getPartyPreference() {
        return partyPreference;
    }

    public void setPartyPreference(PartyPreference partyPreference) {
        this.partyPreference = partyPreference;
    }

    public int getNumDemo() {
        return numDemo;
    }

    public void setNumDemo(int numDemo) {
        this.numDemo = numDemo;
    }

    public int getNumRep() {
        return numRep;
    }

    public void setNumRep(int numRep) {
        this.numRep = numRep;
    }

    public int getVotes() {
        return votes;
    }

    public String getName() {
        return name;
    }

    public Demographics getDemogrpahics() {
        return demogrpahics;
    }

    public void setDemogrpahics(Demographics demogrpahics) {
        this.demogrpahics = demogrpahics;
    }

    public int[] getTempNs() {
        return tempNs;
    }

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("state: "+state.toString()+"\n");
        stringBuffer.append("population: "+population+"\n");
        stringBuffer.append("majority: "+majority.toString()+"\n");
        stringBuffer.append("partyPreference: "+partyPreference.toString()+"\n");
        return stringBuffer.toString();
    }
}
