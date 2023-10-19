package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryDistrict {
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("disBl")
    @Expose
    private String disBl;
    @SerializedName("id")
    @Expose
    private String id;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDisBl() {
        return disBl;
    }

    public void setDisBl(String disBl) {
        this.disBl = disBl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
