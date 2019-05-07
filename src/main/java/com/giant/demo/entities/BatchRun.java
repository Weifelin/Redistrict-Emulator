package com.giant.demo.entities;

import javax.persistence.*;
import java.util.Queue;

@Entity
public class BatchRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int batchRunID;
    @OneToMany
    private Queue<Job> jobList;

    public BatchRun(Queue<Job> jobList) {
        this.jobList = jobList;
    }

    public BatchRun() {
    }

    public int getBatchRunID() {
        return batchRunID;
    }

    public Job getJob(){
        return jobList.poll();
    }
}
