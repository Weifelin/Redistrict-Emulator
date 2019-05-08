package com.giant.demo.returnmodels;

import com.giant.demo.enums.StateE;

import java.util.ArrayList;
import java.util.List;

/*A branch of PrecinctCLusterPair*/
public class singleClusterGroup {
    private StateE state;
    private String clusterID;
    private List<Integer> precinctList;

    public singleClusterGroup() {
    }

    public singleClusterGroup(StateE state, String clusterID) {
        this.state = state;
        this.clusterID = clusterID;
        precinctList = new ArrayList<>();
    }

    public void addPrecinctID(int precintID){
        precinctList.add(precintID);
    }
}
