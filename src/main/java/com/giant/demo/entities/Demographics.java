package com.giant.demo.entities;

class Demographics{
    private double african_american;
    private double white;
    private double asian;
    private double latin_american;

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