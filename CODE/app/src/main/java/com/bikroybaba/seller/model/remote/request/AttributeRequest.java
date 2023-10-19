package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeRequest {

    @SerializedName("attributeName")
    @Expose
    private String attributeName;

    public AttributeRequest() {
    }

    public AttributeRequest(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
