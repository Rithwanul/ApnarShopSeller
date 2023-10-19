package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetConfig {

    @SerializedName("UPDATE_TYPE")
    @Expose
    private String updateType;
    @SerializedName("VERSION_CODE")
    @Expose
    private String versionCode;
    @SerializedName("MESSAGE_BODY")
    @Expose
    private String messageBody;
    @SerializedName("MESSAGE_SHOW")
    @Expose
    private String messageShow;

    @SerializedName("PHONE_NO")
    @Expose
    private String phoneNo;
    @SerializedName("EMAIL")
    @Expose
    private String email;

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public String getMessageShow() {
        return messageShow;
    }

    public void setMessageShow(String messageShow) {
        this.messageShow = messageShow;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "GetConfig{" +
                "updateType='" + updateType + '\'' +
                ", versionCode='" + versionCode + '\'' +
                ", messageBody='" + messageBody + '\'' +
                ", messageShow='" + messageShow + '\'' +
                '}';
    }
}
