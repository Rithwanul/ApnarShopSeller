package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Division {

    @SerializedName("division")
    @Expose
    private String divisionName;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;


    public Division(String divisionId) {
        this.divisionId = divisionId;
    }

    public Division() {
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    @Override
    public String toString() {
        return "Division{" +
                "divisionId=" + divisionId +
                ", divisionName='" + divisionName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Division division = (Division) o;

        if (!divisionName.equals(division.divisionName)) return false;
        return divisionId.equals(division.divisionId);
    }

    @Override
    public int hashCode() {
        int result = divisionName.hashCode();
        result = 31 * result + divisionId.hashCode();
        return result;
    }
}
