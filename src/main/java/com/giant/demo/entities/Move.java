package com.giant.demo.entities;

public class Move {
    private Precinct precinct;
    private Cluster from;
    private Cluster to;
    private ObjectiveFunction newOF;
    private boolean finished;

    public Move() {
    }

    public Move(Precinct precinct, Cluster from, Cluster to) {
        this.precinct = precinct;
        this.from = from;
        this.to = to;
        this.finished = false;
    }

    public Precinct getPrecinct() {
        return precinct;
    }

    public void setPrecinct(Precinct precinct) {
        this.precinct = precinct;
    }

    public Cluster getFrom() {
        return from;
    }

    public void setFrom(Cluster from) {
        this.from = from;
    }

    public Cluster getTo() {
        return to;
    }

    public void setTo(Cluster to) {
        this.to = to;
    }

    public ObjectiveFunction getNewOF() {
        return newOF;
    }

    public void setNewOF(ObjectiveFunction newOF) {
        this.newOF = newOF;
    }


    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

