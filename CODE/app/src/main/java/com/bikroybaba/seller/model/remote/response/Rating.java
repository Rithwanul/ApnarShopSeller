package com.bikroybaba.seller.model.remote.response;

import com.bikroybaba.seller.model.entity.Comment;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Rating {

    @SerializedName("deliveryService")
    @Expose
    private String deliveryService;
    @SerializedName("productQuality")
    @Expose
    private String productQuality;
    @SerializedName("averageRating")
    @Expose
    private String averageRating;
    @SerializedName("productPackaging")
    @Expose
    private String productPackaging;
    @SerializedName("totalRating")
    @Expose
    private String totalRating;
    @SerializedName("sellerBehavior")
    @Expose
    private String sellerBehavior;
    @SerializedName("comments")
    @Expose
    private List<Comment> commentList;

    public String getDeliveryService() {
        return deliveryService;
    }

    public String getProductQuality() {
        return productQuality;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public String getProductPackaging() {
        return productPackaging;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public String getSellerBehavior() {
        return sellerBehavior;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "deliveryService='" + deliveryService + '\'' +
                ", productQuality='" + productQuality + '\'' +
                ", averageRating='" + averageRating + '\'' +
                ", productPackaging='" + productPackaging + '\'' +
                ", totalRating='" + totalRating + '\'' +
                ", sellerBehavior='" + sellerBehavior + '\'' +
                ", commentList=" + commentList +
                '}';
    }
}
