package com.giant.demo.jobObjects;

public class Compactness {
    private double polsPopSlider;
    private double schwartzSlider;
    private double convexHullSlider;
    private double reockSlider;

    public Compactness() {
    }

    public Compactness(double polsPopSlider, double schwartzSlider, double convexHullSlider, double reockSlider) {
        this.polsPopSlider = polsPopSlider;
        this.schwartzSlider = schwartzSlider;
        this.convexHullSlider = convexHullSlider;
        this.reockSlider = reockSlider;
    }

    public double getPolsPopSlider() {
        return polsPopSlider;
    }

    public double getSchwartzSlider() {
        return schwartzSlider;
    }

    public double getConvexHullSlider() {
        return convexHullSlider;
    }

    public double getReockSlider() {
        return reockSlider;
    }

    public void setPolsPopSlider(double polsPopSlider) {
        this.polsPopSlider = polsPopSlider;
    }

    public void setSchwartzSlider(double schwartzSlider) {
        this.schwartzSlider = schwartzSlider;
    }

    public void setConvexHullSlider(double convexHullSlider) {
        this.convexHullSlider = convexHullSlider;
    }

    public void setReockSlider(double reockSlider) {
        this.reockSlider = reockSlider;
    }
}

