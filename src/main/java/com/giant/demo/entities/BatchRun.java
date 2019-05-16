package com.giant.demo.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class BatchRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchRunID;
    @Transient
    private List<Job> jobList;
    @Transient
    private int count;

    public BatchRun(List<Job> jobList) {
        this.jobList = jobList;
        this.count = 0;
    }

    public BatchRun() {
    }

    public int getBatchRunID() {
        return batchRunID;
    }

    public Job getJob(){
        Job job = jobList.get(count);
        return job;
    }
}
