package com.bikroybaba.seller.model.remote.request;

import com.bikroybaba.seller.model.remote.response.Schedule;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {
    @SerializedName("minimumBuy")
    @Expose
    private String minimumBuy;
    @SerializedName("schedule")
    @Expose
    private Schedule schedule;
    @SerializedName("maxDeliveryTime")
    @Expose
    private String maxDeliveryTime;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("weightLimit")
    @Expose
    private String weightLimit;

    public Settings() {
    }

    public Settings(String minimumBuy, Schedule schedule, String deliveryCharge, String weightLimit) {
        this.minimumBuy = minimumBuy;
        this.schedule = schedule;
        this.deliveryCharge = deliveryCharge;
        this.weightLimit = weightLimit;
    }

    public void setMaxDeliveryTime(String maxDeliveryTime) {
        this.maxDeliveryTime = maxDeliveryTime;
    }

    public String getMinimumBuy() {
        return minimumBuy;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getMaxDeliveryTime() {
        return maxDeliveryTime;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public String getWeightLimit() {
        return weightLimit;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "minimumBuy='" + minimumBuy + '\'' +
                ", schedule=" + schedule +
                ", maxDeliveryTime='" + maxDeliveryTime + '\'' +
                ", deliveryCharge='" + deliveryCharge + '\'' +
                ", weightLimit='" + weightLimit + '\'' +
                '}';
    }
}
