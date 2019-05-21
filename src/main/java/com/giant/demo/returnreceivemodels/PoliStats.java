package com.giant.demo.returnreceivemodels;

public class PoliStats {
    private int totalDemoVotes;
    private int totalRepVotes;
    private double likelyRepSeats;
    private double likelyDemoSeats;

    public PoliStats() {
    }

    public PoliStats(int totalDemoVotes, int totalRepVotes, double likelyRepSeats, double likelyDemoSeats) {
        this.totalDemoVotes = totalDemoVotes;
        this.totalRepVotes = totalRepVotes;
        this.likelyRepSeats = likelyRepSeats;
        this.likelyDemoSeats = likelyDemoSeats;
    }

    public int getTotalDemoVotes() {
        return totalDemoVotes;
    }

    public void setTotalDemoVotes(int totalDemoVotes) {
        this.totalDemoVotes = totalDemoVotes;
    }

    public int getTotalRepVotes() {
        return totalRepVotes;
    }

    public void setTotalRepVotes(int totalRepVotes) {
        this.totalRepVotes = totalRepVotes;
    }

    public double getLikelyRepSeats() {
        return likelyRepSeats;
    }

    public void setLikelyRepSeats(double likelyRepSeats) {
        this.likelyRepSeats = likelyRepSeats;
    }

    public double getLikelyDemoSeats() {
        return likelyDemoSeats;
    }

    public void setLikelyDemoSeats(double likelyDemoSeats) {
        this.likelyDemoSeats = likelyDemoSeats;
    }
}
