package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("categoryTitle")
    @Expose
    private String categoryTitle;
    @SerializedName("categoryId")
    @Expose
    private String categoryId;

    public Category() {
    }

    public Category(String categoryTitle, String categoryId) {
        this.categoryTitle = categoryTitle;
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
