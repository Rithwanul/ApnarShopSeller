package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnitType {

    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("unitTypeId")
    @Expose
    private String unitTypeId;

    public String getUnitType() {
        return unitType;
    }

    public String getUnitTypeId() {
        return unitTypeId;
    }

    public UnitType() {
    }

    public UnitType(String unitType, String unitTypeId) {
        this.unitType = unitType;
        this.unitTypeId = unitTypeId;
    }

    @Override
    public String toString() {
        return "UnitType{" +
                "unitType='" + unitType + '\'' +
                ", unitTypeId='" + unitTypeId + '\'' +
                '}';
    }
}
