package com.bikroybaba.seller.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Area {
    @SerializedName("areaId")
    @Expose
    private String areaId;
    @SerializedName("area")
    @Expose
    private String areaName;

    public Area() {
    }

    public Area(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (!areaId.equals(area.areaId)) return false;
        return areaName.equals(area.areaName);
    }

    @Override
    public int hashCode() {
        int result = areaId.hashCode();
        result = 31 * result + areaName.hashCode();
        return result;
    }
}
