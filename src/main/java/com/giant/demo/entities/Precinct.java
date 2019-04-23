package com.giant.demo.entities;

import com.giant.demo.enums.Race;
import com.giant.demo.enums.StateE;
import org.locationtech.jts.geom.Geometry;

import java.util.Set;

public class Precinct {
    private String precinctID;
    private StateE state;
    private int population;
    private Race majority;
    private Geometry boundaries;
    private Set<Precinct> neighbours;



    public Precinct() {
    }

    public Precinct(String precinctID, StateE state, int population, Race majority, Geometry boundaries) {
        this.precinctID = precinctID;
        this.state = state;
        this.population = population;
        this.majority = majority;
        this.boundaries = boundaries;
    }

    public String getPrecinctID() {
        return precinctID;
    }

    public void setPrecinctID(String precinctID) {
        this.precinctID = precinctID;
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
}
