package com.bikroybaba.seller.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Brand {
    @SerializedName("brandId")
    @Expose
    private String brandId;
    @SerializedName("brandTitle")
    @Expose
    private String brandTitle;

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandTitle() {
        return brandTitle;
    }

    public void setBrandTitle(String brandTitle) {
        this.brandTitle = brandTitle;
    }
}
