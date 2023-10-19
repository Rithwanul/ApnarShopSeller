package com.bikroybaba.seller.model.remote.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderDetailsResponse implements Parcelable {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("voucher")
    @Expose
    private String voucher;
    @SerializedName("orderProducts")
    @Expose
    private List<OrderProducts> orderProducts = null;
    @SerializedName("freeDelivery")
    @Expose
    private String freeDelivery;
    @SerializedName("deliveryCharge")
    @Expose
    private String deliveryCharge;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("subTotal")
    @Expose
    private String subTotal;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("maxDeliveryTime")
    @Expose
    private String maxDeliveryTime;

    public static final Creator<OrderDetailsResponse> CREATOR = new Creator<OrderDetailsResponse>() {
        @Override
        public OrderDetailsResponse createFromParcel(Parcel in) {
            return new OrderDetailsResponse(in);
        }

        @Override
        public OrderDetailsResponse[] newArray(int size) {
            return new OrderDetailsResponse[size];
        }
    };

    protected OrderDetailsResponse(Parcel in) {
        address = in.readString();
        voucher = in.readString();
        freeDelivery = in.readString();
        deliveryCharge = in.readString();
        discount = in.readString();
        subTotal = in.readString();
        transactionId = in.readString();
        paymentType = in.readString();
        profilePicture = in.readString();
        createdAt = in.readString();
        total = in.readString();
        phone = in.readString();
        name = in.readString();
        id = in.readString();
        status = in.readString();
        comment = in.readString();
        maxDeliveryTime = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(voucher);
        dest.writeString(freeDelivery);
        dest.writeString(deliveryCharge);
        dest.writeString(discount);
        dest.writeString(subTotal);
        dest.writeString(transactionId);
        dest.writeString(paymentType);
        dest.writeString(profilePicture);
        dest.writeString(createdAt);
        dest.writeString(total);
        dest.writeString(phone);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(status);
        dest.writeString(comment);
        dest.writeString(maxDeliveryTime);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public List<OrderProducts> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProducts> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getFreeDelivery() {
        return freeDelivery;
    }

    public void setFreeDelivery(String freeDelivery) {
        this.freeDelivery = freeDelivery;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getMaxDeliveryTime() {
        return maxDeliveryTime;
    }

    public void setMaxDeliveryTime(String maxDeliveryTime) {
        this.maxDeliveryTime = maxDeliveryTime;
    }
}
