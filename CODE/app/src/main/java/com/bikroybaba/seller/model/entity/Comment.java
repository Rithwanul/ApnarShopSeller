package com.bikroybaba.seller.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("buyer")
    @Expose
    private String buyerName;
    @SerializedName("comment")
    @Expose
    private String buyerComment;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getBuyerName() {
        return buyerName;
    }

    public String getBuyerComment() {
        return buyerComment;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "buyerName='" + buyerName + '\'' +
                ", buyerComment='" + buyerComment + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
}
