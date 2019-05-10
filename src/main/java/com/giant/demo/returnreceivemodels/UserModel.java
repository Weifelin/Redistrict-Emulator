package com.giant.demo.returnreceivemodels;

import com.giant.demo.enums.UserType;

public class UserModel {
    private String username;
    private UserType userType;
    private String password;
    private String salt;

    public UserModel() {
    }

    public UserModel(String username, UserType userType, String password, String salt) {
        this.username = username;
        this.userType = userType;
        this.password = password;
        this.salt = salt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
