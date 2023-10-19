package com.bikroybaba.seller.model.remote.response;

import com.bikroybaba.seller.model.entity.Brand;
import com.bikroybaba.seller.model.entity.Image;
import com.bikroybaba.seller.model.remote.request.Category;
import com.bikroybaba.seller.model.remote.request.ProductType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductDetails {
    @SerializedName("unitWeight")
    @Expose
    private ProductUnitWeight unitWeight;
    @SerializedName("unitPrice")
    @Expose
    private String unitPrice;
    @SerializedName("images")
    @Expose
    private List<Image> images = null;
    @SerializedName("productId")
    @Expose
    private String productId;
    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("type")
    @Expose
    private ProductType type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("unit")
    @Expose
    private ProductUnit unit;
    @SerializedName("totalQuantity")
    @Expose
    private String totalQuantity;
    @SerializedName("size")
    @Expose
    private String size;
    @SerializedName("supplier")
    @Expose
    private Supplier supplier;
    @SerializedName("category")
    @Expose
    private Category category;
    @SerializedName("brand")
    @Expose
    private Brand brand;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;

    @SerializedName("attributes")
    @Expose
    private ArrayList<AttributeProductEditResponse> attributes;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public ProductDetails() {
    }

    public ProductUnitWeight getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(ProductUnitWeight unitWeight) {
        this.unitWeight = unitWeight;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ProductUnit getUnit() {
        return unit;
    }

    public void setUnit(ProductUnit unit) {
        this.unit = unit;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<AttributeProductEditResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<AttributeProductEditResponse> attributes) {
        this.attributes = attributes;
    }
}
