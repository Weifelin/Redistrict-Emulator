package com.giant.demo.entities;

import com.giant.demo.enums.UserType;

import javax.persistence.*;
import java.util.concurrent.atomic.AtomicInteger;

/*This is representation of user table in db.*/
/*JPA Entity is defined with @Entity annotation, represent a table in database.*/
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String userName;
    private long userID;
    private UserType userType;

    private String password;

    @Transient
    private String passwordConfirm;
    private static AtomicInteger ID_GENERATOR = new AtomicInteger(10000);

    public User(){

    }

    public User(String userName, UserType userType, String password) {
        this.userName = userName;
        this.userType = userType;
        this.password = password;
        this.userID = ID_GENERATOR.getAndIncrement();
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
