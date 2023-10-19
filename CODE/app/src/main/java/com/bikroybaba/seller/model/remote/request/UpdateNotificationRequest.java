package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateNotificationRequest {

    @SerializedName("profileNotificationId")
    @Expose
    private final String notificationId;
    @SerializedName("readStatus")
    @Expose
    private final String readStatus;
    @SerializedName("isDeleted")
    @Expose
    private final String isDeleted;

    public UpdateNotificationRequest(String notificationId, String readStatus, String isDeleted) {
        this.notificationId = notificationId;
        this.readStatus = readStatus;
        this.isDeleted = isDeleted;
    }
}
