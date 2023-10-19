package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDivision {
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("divBl")
    @Expose
    private String divBl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("divisionId")
    @Expose
    private String divisionId;

    public DeliveryDivision(String division, String divisionId) {
        this.division = division;
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDivBl() {
        return divBl;
    }

    public void setDivBl(String divBl) {
        this.divBl = divBl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }
}
