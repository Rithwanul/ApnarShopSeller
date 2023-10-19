package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassword {

    @SerializedName("oldPin")
    @Expose
    private final String oldPassword;
    @SerializedName("newPin")
    @Expose
    private final String newPassword;

    public ChangePassword(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
