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

    public void combine(Demographics d){
        int pop = this.population + d.getPopulation();
        this.african_american = Math.round((this.african_american * this.population + d.getAfricanAmerican() * d.getPopulation()) / (double) pop * 100) / 100.0;
        this.asian = Math.round((this.asian * this.population + d.getAsian() * d.getPopulation()) / (double) pop * 100) / 100.0;
        this.latin_american = Math.round((this.latin_american * this.population + d.getLatinAmerican() * d.getPopulation()) / (double) pop * 100) / 100.0;
        this.white = Math.round((this.white * this.population + d.getWhite() * d.getPopulation()) / (double) pop * 100) / 100.0;
        this.other = Math.round((this.other * this.population + d.getOther() * d.getPopulation()) / (double) pop * 100) / 100.0;
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

    public String toString(){
        return african_american + "\n" +
                asian + "\n" +
                latin_american + "\n" +
                white + "\n" +
                other + "\n";

    }
}