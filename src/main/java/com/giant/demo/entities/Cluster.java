package com.giant.demo.entities;

import com.giant.demo.enums.PartyPreference;
import org.locationtech.jts.geom.Geometry;

import javax.persistence.*;
import java.util.*;

@Entity
public class Cluster {
    @Id
    private int clusterID;
    public int level;
    @OneToMany
    private List<Precinct> containedPrecincts;
    private Geometry boundary;
    @OneToOne
    private ObjectiveFunction objectiveFunction;
    @Transient
    private List<ClusterEdge> edges;
    private int population;
    @OneToOne
    private Demographics demographics;
    private PartyPreference partyPreference;
    @Transient
    private Set<String> counties = new HashSet<>();
    private boolean isMajorityMinority;
    private int numDemo;
    private int numRep;
    private int votes;
    @Transient
    private Map<Integer, ClusterEdge> clusterEdgeMap;



    public Cluster(int clusterID) {
        this.clusterID = clusterID;
    }

    public Cluster(int clusterID, List<Precinct> containedPrecincts) {
        this.clusterID = clusterID;
        this.containedPrecincts = containedPrecincts;
        this.edges = new ArrayList<>();
        Precinct p = containedPrecincts.get(0);
        this.population = p.getPopulation();
        this.demographics = p.getDemogrpahics();
        this.counties.add(p.getCountyID());
        this.numDemo = p.getNumDemo();
        this.numRep = p.getNumRep();
        this.votes = p.getVotes();
        this.boundary = p.getBoundaries();
        this.level = 0;
        this.clusterEdgeMap = new HashMap<>();
    }

    public boolean mergeInto(Cluster c){
        return false;
    }

    public void destory(){

    }

    public boolean updateBoundary(){
        return false;
    }

    public Demographics getDemographics(){ return demographics; }

    public Precinct getNextPrecinct(){
        return null;
    }

    public int getClusterID() {
        return clusterID;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public List<Precinct> getContainedPrecincts() {
        return containedPrecincts;
    }

    public void setContainedPrecincts(ArrayList<Precinct> containedPrecincts) {
        this.containedPrecincts = containedPrecincts;
    }

    public Geometry getBoundary() {
        return boundary;
    }

    public void setBoundary(Geometry boundary) {
        this.boundary = boundary;
    }

    public ObjectiveFunction getObjectiveFunction() {
        return objectiveFunction;
    }

    public void setObjectiveFunction(ObjectiveFunction objectiveFunction) {
        this.objectiveFunction = objectiveFunction;
    }

    public int getPopulation(){
        return this.population;
    }
    public void setPopulation(int pop){ this.population = pop;}


    public List<ClusterEdge> getEdges() {
        return edges;
    }

    public void addEdge(ClusterEdge edge){
        this.edges.add(edge);
    }

    public void setEdges(List<ClusterEdge> edges) {
        this.edges = edges;
    }

    public void combineEdges(Cluster c2){//return back to normal

        Set<Integer> keys = c2.getClusterEdgeMap().keySet();
        for(int key : keys){
            c2.getClusterEdgeMap().get(key).getCluster2().getClusterEdgeMap().remove(c2.getClusterID());
            //if(!this.getClusterEdgeMap().containsKey(c2.getClusterEdgeMap().get(key).getCluster2().getClusterID())){
                //ClusterEdge edge = new ClusterEdge(c2.getClusterEdgeMap().get(key).getCluster2(), this);
                //c2.getClusterEdgeMap().get(key).getCluster2().getClusterEdgeMap().put(c2.getClusterEdgeMap().get(key).getCluster1().getClusterID(), edge);
                //this.getClusterEdgeMap().put(c2.getClusterEdgeMap().get(key).getCluster2().getClusterID(), new ClusterEdge(this, c2.getClusterEdgeMap().get(key).getCluster2()));
           // }
            c2.getClusterEdgeMap().get(key).getCluster2().getClusterEdgeMap().remove(c2.getClusterID());
        }
    }

    public void addPopulation(int pop){
        this.population += pop;
    }

    public void addPrecinct(Precinct p){
        this.addPopulation(p.getPopulation());
        this.counties.add(p.getCountyID());
        this.containedPrecincts.add(p);
        this.numDemo += p.getNumDemo();
        this.numRep += p.getNumRep();
        this.votes += p.getVotes();
        this.boundary.union(p.getBoundaries());
    }

    public void sortEdges(){
        Collections.sort(this.edges, (e1, e2) -> e1.compareTo(e2));
    }

    public ClusterEdge findClusterPair(int numClusters, int totalPop, Job j){
        double max = 0.0;
        ClusterEdge bestEdge = null;
        double popUpperBound = totalPop / ((double)numClusters / 2.0) * 1.2;
        for(ClusterEdge e : this.edges){
            int combinePop = e.getCluster1().getPopulation() + e.getCluster2().getPopulation();
            if(bestEdge == null || combinePop <= popUpperBound && e.getJoinability(j) > max){
                max = e.getJoinability(j);
                bestEdge = e;
            }
        }
        this.sortEdges();
        return bestEdge;
    }

    public void combineCluster(Cluster c2){
        this.addPopulation(c2.getPopulation());
        this.containedPrecincts.addAll(c2.getContainedPrecincts());
        this.demographics.combine(c2.getDemographics());
        this.combineEdges(c2);
    }

    public int compareTo(Cluster c2){
        if(this.population == c2.getPopulation())
            return 0;
        return (this.population > c2.getPopulation()) ? -1 : 1;
    }

    public PartyPreference getPartyPreference() {
        return partyPreference;
    }

    public void setPartyPreference(PartyPreference partyPreference) {
        this.partyPreference = partyPreference;
    }

    public void setDemographics(Demographics demographics) {
        this.demographics = demographics;
    }

    public boolean isMajorityMinority() {
        int pop = demographics.getPopulation();
        int white = pop - (int) (demographics.getLatinAmerican()*pop) - (int) (demographics.getAsian()*pop) - (int)(demographics.getAfricanAmerican()*pop);
        if(white < demographics.getLatinAmerican() || white < demographics.getAsian() || white < demographics.getAfricanAmerican())
            return true;
        return false;
    }

    public Precinct getPrecinct(int n){
        for(Precinct p : this.getContainedPrecincts()){
            if(p.getPrecinctID() == n)
                return p;
        }
        return null;
    }

    public String toString(){
        String ret = "ClusterID: " + clusterID +
                "\nPopulation:  " + population +
                "\nEdges: " + edges +
                "\nPrecincts: " + containedPrecincts;
        return ret;
    }

    public Map<Integer, ClusterEdge> getClusterEdgeMap() {
        return clusterEdgeMap;
    }

    public void setClusterEdgeMap(Map<Integer, ClusterEdge> clusterEdgeMap) {
        this.clusterEdgeMap = clusterEdgeMap;
    }
}
