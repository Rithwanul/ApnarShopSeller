package com.bikroybaba.seller.model.entity;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Attribute extends BaseModel{

    @SerializedName("attributeName")
    @Expose
    private String attributeName;

    @SerializedName("attributeDataName")
    @Expose
    private String attributeDataName;

    @SerializedName("status")
    @Expose
    private String status;

    public Attribute() {
    }

    public Attribute(String attributeName, String status) {
        this.attributeName = attributeName;
        this.status = status;
    }

    public Attribute(int id, Date createdAt, Date updatedAt, String attributeName, String status) {
        super(id, createdAt, updatedAt);
        this.attributeName = attributeName;
        this.status = status;
    }

    public Attribute(String attributeName, String attributeDataName, String status) {
        this.attributeName = attributeName;
        this.attributeDataName = attributeDataName;
        this.status = status;
    }

    public Attribute(int id, Date createdAt, Date updatedAt, String attributeName, String attributeDataName, String status) {
        super(id, createdAt, updatedAt);
        this.attributeName = attributeName;
        this.attributeDataName = attributeDataName;
        this.status = status;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeDataName() {
        return attributeDataName;
    }

    public void setAttributeDataName(String attributeDataName) {
        this.attributeDataName = attributeDataName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj){
            return true;
        }

        if (obj == null && getClass() != obj.getClass()){
            return false;
        }

        Attribute attribute = (Attribute) obj;

        if (attribute.getAttributeDataName() != null){
            return getAttributeName().equals(attribute.attributeName) &&
                    getAttributeDataName().equals(attribute.getAttributeDataName());
        }else {
            return getAttributeName().equals(attribute.attributeName);
        }
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
