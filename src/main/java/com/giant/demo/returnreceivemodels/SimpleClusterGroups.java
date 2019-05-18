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

    private List<SingleClusterGroup> simpleClusterGroups;

    public SimpleClusterGroups() {
        simpleClusterGroups = new ArrayList<>();
    }

    public List<SingleClusterGroup> getSimpleClusterGroups() {
        return simpleClusterGroups;
    }

    public void addClusterGroup(SingleClusterGroup singleClusterGroup){
        simpleClusterGroups.add(singleClusterGroup);
    }

    public void setState(StateE state) {
        this.state = state;
    }

    public StateE getState() {
        return state;
    }

    public void setSimpleClusterGroups(List<SingleClusterGroup> simpleClusterGroups) {
        this.simpleClusterGroups = simpleClusterGroups;
    }
}
