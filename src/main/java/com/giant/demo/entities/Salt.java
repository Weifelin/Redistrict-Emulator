package com.giant.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Salt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int saltID;
    private String salt;

    public Salt() {
    }

    public Salt(String salt) {
        this.salt = salt;
    }

    public String getSaltString() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


}
