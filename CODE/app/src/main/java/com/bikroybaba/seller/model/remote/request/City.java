package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("city")
    @Expose
    private String cityName;

    public City() {
    }

    public City(String cityId) {
        this.cityId = cityId;
    }

    public City(String cityId, String cityName) {
        this.cityId = cityId;
        this.cityName = cityName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        City city = (City) o;

        if (!cityId.equals(city.cityId)) return false;
        return cityName.equals(city.cityName);
    }

    @Override
    public int hashCode() {
        int result = cityId.hashCode();
        result = 31 * result + cityName.hashCode();
        return result;
    }

}
