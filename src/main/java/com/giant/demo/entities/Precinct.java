package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Precinct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int precinctID;
    private StateE state;
    private int population;
    private Race majority;
    private Geometry boundaries;
    @ManyToMany
    @JoinColumn(name = "precinctID")
    private Set<Precinct> neighbours;
    private PartyPreference partyPreference;
    private int numDemo;
    private int numRep;
    private int votes;
    private String name;

    public Precinct(int precinctID, String name, int pop, int votes, double demo, double rep, Geometry polygon){
        this.precinctID = precinctID;
        this.name = name;
        this.population = pop;
        this.votes = votes;
        this.numDemo = (int) demo;
        this.numRep = (int) rep;
        this.boundaries = polygon;

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
}
