package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {
    @SerializedName("sat")
    @Expose
    private String sat;
    @SerializedName("sun")
    @Expose
    private String sun;
    @SerializedName("mon")
    @Expose
    private String mon;
    @SerializedName("tue")
    @Expose
    private String tue;
    @SerializedName("wed")
    @Expose
    private String wed;
    @SerializedName("thu")
    @Expose
    private String thu;
    @SerializedName("fri")
    @Expose
    private String friday;

    public Schedule() {
    }

    public Schedule(String sat, String sun, String mon, String tue, String wed, String thu, String friday) {
        this.sat = sat;
        this.sun = sun;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.friday = friday;
    }

    public String getSat() {
        return sat;
    }

    public String getSun() {
        return sun;
    }

    public String getMon() {
        return mon;
    }

    public String getTue() {
        return tue;
    }

    public String getWed() {
        return wed;
    }

    public String getThu() {
        return thu;
    }

    public String getFriday() {
        return friday;
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "sat='" + sat + '\'' +
                ", sun='" + sun + '\'' +
                ", mon='" + mon + '\'' +
                ", tue='" + tue + '\'' +
                ", wed='" + wed + '\'' +
                ", thu='" + thu + '\'' +
                ", friday='" + friday + '\'' +
                '}';
    }
}
