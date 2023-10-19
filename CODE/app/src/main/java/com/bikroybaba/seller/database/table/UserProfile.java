package com.bikroybaba.seller.database.table;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_profile")
public class UserProfile {

    public UserProfile(@NonNull String userId, String userName, String phoneNumber,
                       String userProfileImage, String email, String shopAddress, String shopType,
                       String shopName, String city, String area, String createdAt,String shopId) {
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.userProfileImage = userProfileImage;
        this.email = email;
        this.shopAddress = shopAddress;
        this.shopType = shopType;
        this.shopName = shopName;
        this.city = city;
        this.area = area;
        this.createdAt = createdAt;
        this.shopId =shopId;
    }

    @PrimaryKey
    @NonNull
    private final String userId;
    private final String userName;
    private final String phoneNumber;
    private final String userProfileImage;
    private final String email;
    private final String shopAddress;
    private final String shopType;
    private final String shopName;
    private final String city;
    private final String area;
    private final String createdAt;
    private final String shopId;

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public String getEmail() {
        return email;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public String getShopType() {
        return shopType;
    }

    public String getShopName() {
        return shopName;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getShopId() {
        return shopId;
    }
}
