package com.giant.demo.entities;

import com.giant.demo.enums.UserType;

import javax.persistence.*;

/*This is representation of user table in db.*/
/*JPA Entity is defined with @Entity annotation, represent a table in database.*/
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    private String username;
    private String password;
    private UserType userType;

    @Transient/*Ignore confirm password when putting into data.*/
    private String passwordConfirm;

    public User(){

    }

    public User(String username, UserType userType, String password) {
        this.username = username;
        this.userType = userType;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}
