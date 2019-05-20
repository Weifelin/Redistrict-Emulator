package com.giant.demo.returnreceivemodels;

public class PoliStats {
    private int totalDemoVotes;
    private int totalRepVotes;
    private int likelyRepSeats;
    private int likelyDemoSeats;

    public PoliStats() {
    }

    public PoliStats(int totalDemoVotes, int totalRepVotes, int likelyRepSeats, int likelyDemoSeats) {
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

    public int getLikelyRepSeats() {
        return likelyRepSeats;
    }

    public void setLikelyRepSeats(int likelyRepSeats) {
        this.likelyRepSeats = likelyRepSeats;
    }

    public int getLikelyDemoSeats() {
        return likelyDemoSeats;
    }

    public void setLikelyDemoSeats(int likelyDemoSeats) {
        this.likelyDemoSeats = likelyDemoSeats;
    }
}
