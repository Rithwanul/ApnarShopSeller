package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendDeliveryArea {
    @SerializedName("division")
    @Expose
    private String divisionId;
    @SerializedName("district")
    @Expose
    private String districtId;
    @SerializedName("city")
    @Expose
    private String cityId;
    @SerializedName("area")
    @Expose
    private String areaId;

    public String getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public String toString() {
        return "SendDeliveryArea{" +
                "divisionId='" + divisionId + '\'' +
                ", districtId='" + districtId + '\'' +
                ", cityId='" + cityId + '\'' +
                ", areaId='" + areaId + '\'' +
                '}';
    }
}
