package com.giant.demo.entities;

import com.giant.demo.enums.Race;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Demographics{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int demoID;
    private double african_american;
    private double asian;
    private double latin_american;
    private double white;
    private double other;
    private int population;
    @Transient
    private Set<Race> races;

    public Demographics() {
    }

    public Demographics(double aa, double a, double la, double w, double o, int pop){
        this.african_american = aa;
        this.asian = a;
        this.latin_american = la;
        this.white = w;
        this.other = o;
        this.population = pop;
    }

    public double getAfricanAmerican(){
        return african_american;
    }
    public double getAsian(){
        return asian;
    }
    public double getLatinAmerican(){
        return latin_american;
    }

    public Set<Race> getRaces() {
        return races;
    }

    public void setRaces(Set<Race> races) {
        this.races = races;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getWhite() {
        return white;
    }

    public void setWhite(double white) {
        this.white = white;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }
}