package com.giant.demo.returnreceivemodels;

public class MoveModel {

    private boolean finished;
    private int fromID;
    private int toId;
    private int precinctID;

    public MoveModel() {
        this.finished = true;
    }

    public MoveModel(int fromID, int toId, int precinctID, boolean finished) {
        this.fromID = fromID;
        this.toId = toId;
        this.precinctID = precinctID;
        this.finished = finished;
    }

    public int getFromID() {
        return fromID;
    }

    public void setFromID(int fromID) {
        this.fromID = fromID;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }

    public int getPrecinctID() {
        return precinctID;
    }

    public void setPrecinctID(int precinctID) {
        this.precinctID = precinctID;
    }

    public boolean isFinished() {
        return finished;
    }
}
