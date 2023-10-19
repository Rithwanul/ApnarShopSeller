package com.bikroybaba.seller.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryCity {
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("cityBl")
    @Expose
    private String cityBl;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("cityId")
    @Expose
    private String cityId;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityBl() {
        return cityBl;
    }

    public void setCityBl(String cityBl) {
        this.cityBl = cityBl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }
}
