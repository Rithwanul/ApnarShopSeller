package com.bikroybaba.seller.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryArea {
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("areaId")
    @Expose
    private String areaId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("areaBl")
    @Expose
    private String areaBl;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAreaBl() {
        return areaBl;
    }

    public void setAreaBl(String areaBl) {
        this.areaBl = areaBl;
    }
}
