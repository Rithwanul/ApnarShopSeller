package com.bikroybaba.seller.model.entity;

import com.bikroybaba.seller.model.remote.response.DeliveryDistrict;
import com.bikroybaba.seller.model.remote.response.DeliveryDivision;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Delivery {
    @SerializedName("cities")
    @Expose
    private List<DeliveryCity> cities = null;
    @SerializedName("districts")
    @Expose
    private List<DeliveryDistrict> districts = null;
    @SerializedName("areas")
    @Expose
    private List<DeliveryArea> areas = null;
    @SerializedName("divisions")
    @Expose
    private List<DeliveryDivision> divisions = null;

    public List<DeliveryCity> getCities() {
        return cities;
    }

    public void setCities(List<DeliveryCity> cities) {
        this.cities = cities;
    }

    public List<DeliveryDistrict> getDistricts() {
        return districts;
    }

    public void setDistricts(List<DeliveryDistrict> districts) {
        this.districts = districts;
    }

    public List<DeliveryArea> getAreas() {
        return areas;
    }

    public void setAreas(List<DeliveryArea> areas) {
        this.areas = areas;
    }

    public List<DeliveryDivision> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<DeliveryDivision> divisions) {
        this.divisions = divisions;
    }
}
