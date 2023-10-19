package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateDeliveryArea {
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("id")
    @Expose
    private String id;

    public UpdateDeliveryArea(String division, String area, String city, String district, String id) {
        this.division = division;
        this.area = area;
        this.city = city;
        this.district = district;
        this.id = id;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
