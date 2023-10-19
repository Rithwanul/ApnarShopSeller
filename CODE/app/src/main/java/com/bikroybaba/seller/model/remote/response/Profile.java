package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {

    @SerializedName("sellerId")
    @Expose
    private String sellerId;
    @SerializedName("sellerName")
    @Expose
    private String sellerName;
    @SerializedName("contactNo")
    @Expose
    private String sellerPhoneNumber;
    @SerializedName("address")
    @Expose
    private String shopAddress;
    @SerializedName("imageUrl")
    @Expose
    private String sellerImage;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("shopType")
    @Expose
    private String shopType;
    @SerializedName("shopName")
    @Expose
    private String shopName;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("webAddress")
    @Expose
    private String webAddress;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("shopId")
    @Expose
    private String shopId;

    public String getShopId() {
        return shopId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerPhoneNumber() {
        return sellerPhoneNumber;
    }

    public void setSellerPhoneNumber(String sellerPhoneNumber) {
        this.sellerPhoneNumber = sellerPhoneNumber;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWebAddress() {
        return webAddress;
    }

    public void setWebAddress(String webAddress) {
        this.webAddress = webAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
