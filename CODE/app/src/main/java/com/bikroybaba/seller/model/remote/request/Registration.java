package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registration {

    @SerializedName("sellerName")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phoneNumber;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("shopType")
    @Expose
    private String shopType;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("areaId")
    @Expose
    private String areaId;
    @SerializedName("address")
    @Expose
    private String address;

    public Registration() {
    }

    public Registration(String name, String phoneNumber, String email, String shopType, String shopName, String areaId, String address) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.shopType = shopType;
        this.shopName = shopName;
        this.areaId = areaId;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getShopType() {
        return shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Registration{" +
                "name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", shopType='" + shopType + '\'' +
                ", shopName='" + shopName + '\'' +
                ", areaId='" + areaId + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
