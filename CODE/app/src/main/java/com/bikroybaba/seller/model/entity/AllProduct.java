package com.bikroybaba.seller.model.entity;

import com.bikroybaba.seller.model.remote.response.Offers;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllProduct {
    @SerializedName("unitPrice")
    @Expose
    private String unitPrice;
    @SerializedName("offers")
    @Expose
    private List<Offers> offers = null;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("unitQuantity")
    @Expose
    private String unitQuantity;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("remaining")
    @Expose
    private String remaining;
    @SerializedName("status")
    @Expose
    private String status;

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Offers> getOffers() {
        return offers;
    }

    public void setOffers(List<Offers> offers) {
        this.offers = offers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemaining() {
        return remaining;
    }

    public void setRemaining(String remaining) {
        this.remaining = remaining;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AllProduct{" +
                "unitPrice='" + unitPrice + '\'' +
                ", offers=" + offers +
                ", image='" + image + '\'' +
                ", unit='" + unit + '\'' +
                ", unitQuantity='" + unitQuantity + '\'' +
                ", id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", remaining='" + remaining + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
