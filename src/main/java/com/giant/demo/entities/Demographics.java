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
    @Transient
    private Set<Race> races;

    public Demographics() {
    }

    public Demographics(double aa, double a, double la){
        this.african_american = aa;
        this.asian = a;
        this.latin_american = la;
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
}