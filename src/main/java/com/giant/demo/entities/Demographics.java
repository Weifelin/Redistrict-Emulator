package com.giant.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Demographics{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int demoID;
    private double african_american;
    private double white;
    private double asian;
    private double latin_american;

    public Demographics() {
    }

    public Demographics(double aa, double w, double a, double la){
        this.african_american = aa;
        this.white = w;
        this.asian = a;
        this.latin_american = la;
    }

    public double getAfricanAmerican(){
        return african_american;
    }
    public double getWhite(){
        return white;
    }
    public double getAsian(){
        return asian;
    }
    public double getLatinAmerican(){
        return latin_american;
    }
}