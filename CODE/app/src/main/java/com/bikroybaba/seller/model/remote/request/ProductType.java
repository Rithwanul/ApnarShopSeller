package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductType {

    @SerializedName("typeId")
    @Expose
    private String typeId;
    @SerializedName("typeTitle")
    @Expose
    private String typeTitle;

    public ProductType() {
    }

    public ProductType(String typeId, String typeTitle) {
        this.typeId = typeId;
        this.typeTitle = typeTitle;
    }


    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeTitle() {
        return typeTitle;
    }

    public void setTypeTitle(String typeTitle) {
        this.typeTitle = typeTitle;
    }
}
