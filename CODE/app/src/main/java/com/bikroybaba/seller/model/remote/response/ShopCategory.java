package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.SerializedName;

public class ShopCategory {

    @SerializedName("shopCategoryId")
    private int shopCategoryId;
    @SerializedName("shopCategoryTitle")
    private String shopCategoryName;

    public ShopCategory(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    public int getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(int shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryName() {
        return shopCategoryName;
    }

    public void setShopCategoryName(String shopCategoryName) {
        this.shopCategoryName = shopCategoryName;
    }

    @Override
    public String toString() {
        return "ShopCategory{" +
                "shopCategoryId=" + shopCategoryId +
                ", shopCategoryName='" + shopCategoryName + '\'' +
                '}';
    }
}
