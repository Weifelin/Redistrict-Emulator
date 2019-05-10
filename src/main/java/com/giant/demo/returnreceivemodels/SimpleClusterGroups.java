package com.giant.demo.returnreceivemodels;

import com.giant.demo.enums.StateE;

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
public class SimpleClusterGroups {

    private StateE state;

    private List<SingleClusterGroup> simpleClusterGourps;

    public SimpleClusterGroups() {
        simpleClusterGourps = new ArrayList<>();
    }

    public List<SingleClusterGroup> getSimpleClusterGourps() {
        return simpleClusterGourps;
    }

    public void addClusterGroup(SingleClusterGroup singleClusterGroup){
        simpleClusterGourps.add(singleClusterGroup);
    }

    public void setState(StateE state) {
        this.state = state;
    }

}
