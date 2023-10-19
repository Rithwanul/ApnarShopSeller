package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductUnit {
    @SerializedName("unitId")
    @Expose
    private String unitId;
    @SerializedName("unitTitle")
    @Expose
    private String unitTitle;
    @SerializedName("unitQuantity")
    @Expose
    private String unitQuantity;

    public ProductUnit(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }
}
