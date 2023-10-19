package com.bikroybaba.seller.model.remote.response;

import com.bikroybaba.seller.model.entity.Area;
import com.bikroybaba.seller.model.remote.request.City;
import com.bikroybaba.seller.model.remote.request.District;
import com.bikroybaba.seller.model.remote.request.Division;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetDeliveryArea {
    @SerializedName("cities")
    @Expose
    private List<City> cities = null;
    @SerializedName("districts")
    @Expose
    private List<District> districts = null;
    @SerializedName("areas")
    @Expose
    private List<Area> areas = null;
    @SerializedName("divisions")
    @Expose
    private List<Division> divisions = null;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<Division> divisions) {
        this.divisions = divisions;
    }

    @Override
    public String toString() {
        return "GetDeliveryArea{" +
                "cities=" + cities +
                ", districts=" + districts +
                ", areas=" + areas +
                ", divisions=" + divisions +
                '}';
    }
}
