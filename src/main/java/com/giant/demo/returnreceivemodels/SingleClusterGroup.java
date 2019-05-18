package com.giant.demo.returnreceivemodels;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*A branch of PrecinctCLusterPair*/
public class SingleClusterGroup {
    private int clusterID;
    private Set<Integer> precinctList;

    public SingleClusterGroup() {
    }

    public SingleClusterGroup(int clusterID) {
        this.clusterID = clusterID;
        precinctList = new HashSet<>();
    }

    public void addPrecinctID(int precintID){
        precinctList.add(precintID);
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public Set<Integer> getPrecinctList() {
        return precinctList;
    }

    public void setPrecinctList(Set<Integer> precinctList) {
        this.precinctList = precinctList;
    }
}
