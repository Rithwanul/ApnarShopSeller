package com.bikroybaba.seller.model.remote.response;

import com.bikroybaba.seller.model.entity.Attribute;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AttributeResponse {

    @SerializedName("SUCCESS")
    @Expose
    private String success;

    @SerializedName("data")
    @Expose
    private ArrayList<Attribute> attributes;

    @SerializedName("totalElements")
    @Expose
    private int numOfElement;

    public AttributeResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public int getNumOfElement() {
        return numOfElement;
    }
}
