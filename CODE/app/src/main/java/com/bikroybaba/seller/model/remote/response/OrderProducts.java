package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderProducts {
    @SerializedName("offers")
    @Expose
    private List<Offer> offers = null;
    @SerializedName("nameQuantity")
    @Expose
    private String nameQuantity;
    @SerializedName("price")
    @Expose
    private String price;

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getNameQuantity() {
        return nameQuantity;
    }

    public void setNameQuantity(String nameQuantity) {
        this.nameQuantity = nameQuantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
