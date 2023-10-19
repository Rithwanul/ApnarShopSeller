package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogIn {

    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;

    public LogIn(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public LogIn(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LogIn{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
