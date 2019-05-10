package com.giant.demo.returnreceivemodels;

import java.util.ArrayList;
import java.util.List;

/*A branch of PrecinctCLusterPair*/
public class SingleClusterGroup {
    private int clusterID;
    private List<Integer> precinctList;

    public SingleClusterGroup() {
    }

    public SingleClusterGroup(int clusterID) {
        this.clusterID = clusterID;
        precinctList = new ArrayList<>();
    }

    public void addPrecinctID(int precintID){
        precinctList.add(precintID);
    }
}
