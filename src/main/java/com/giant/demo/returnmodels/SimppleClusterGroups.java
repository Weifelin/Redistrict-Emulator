package com.giant.demo.returnmodels;

import java.util.ArrayList;
import java.util.List;

/*This for Sending back right after phrase 1
*
*               simpleClusterGroups
*                    /   ...    \
*                   /            \
*           clusterID1    ...    clusterIDn
*              /   \               /   \
*             / ... \             / ... \
*            /       \           /       \
*  precinctID1...precinctIDn    pre...   precinctIDn
*
* */
public class SimppleClusterGroups {

    private List<singleClusterGroup> simpleClusterGourps;

    public SimppleClusterGroups() {
        simpleClusterGourps = new ArrayList<>();
    }

    public List<singleClusterGroup> getSimpleClusterGourps() {
        return simpleClusterGourps;
    }

    public void addClusterGroup(singleClusterGroup singleClusterGroup){
        simpleClusterGourps.add(singleClusterGroup);
    }
}
