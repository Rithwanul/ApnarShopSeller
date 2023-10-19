package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductCreate {
    @SerializedName("category")
    @Expose
    private String productCategory;
    @SerializedName("type")
    @Expose
    private String productType;
    @SerializedName("brand")
    @Expose
    private String productBrand;
    @SerializedName("supplier")
    @Expose
    private String productSupplier;
    @SerializedName("title")
    @Expose
    private String productTile;
    @SerializedName("size")
    @Expose
    private String productSize;
    @SerializedName("unitType")
    @Expose
    private String unitType;
    @SerializedName("unitQuantity")
    @Expose
    private String unitQuantity;
    @SerializedName("unitWeightType")
    @Expose
    private String unitWeightType;
    @SerializedName("unitWeightQuantity")
    @Expose
    private String unitWeightQuantity;
    @SerializedName("unitPrice")
    @Expose
    private String unitPrice;
    @SerializedName("totalQuantity")
    @Expose
    private String totalQuantity;
    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;
    @SerializedName("description")
    @Expose
    private String productDescription;
   /* @SerializedName("image")
    @Expose
    private List<Image> imageList;*/

    public ProductCreate() {
    }

    public ProductCreate(String productCategory, String productType, String productBrand, String productSupplier, String productTile, String productSize, String unitType, String unitQuantity, String unitWeightType, String unitWeightQuantity, String unitPrice, String totalQuantity, String totalPrice, String productDescription) {
        this.productCategory = productCategory;
        this.productType = productType;
        this.productBrand = productBrand;
        this.productSupplier = productSupplier;
        this.productTile = productTile;
        this.productSize = productSize;
        this.unitType = unitType;
        this.unitQuantity = unitQuantity;
        this.unitWeightType = unitWeightType;
        this.unitWeightQuantity = unitWeightQuantity;
        this.unitPrice = unitPrice;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getProductSupplier() {
        return productSupplier;
    }

    public void setProductSupplier(String productSupplier) {
        this.productSupplier = productSupplier;
    }

    public String getProductTile() {
        return productTile;
    }

    public void setProductTile(String productTile) {
        this.productTile = productTile;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public String getUnitQuantity() {
        return unitQuantity;
    }

    public void setUnitQuantity(String unitQuantity) {
        this.unitQuantity = unitQuantity;
    }

    public String getUnitWeightType() {
        return unitWeightType;
    }

    public void setUnitWeightType(String unitWeightType) {
        this.unitWeightType = unitWeightType;
    }

    public String getUnitWeightQuantity() {
        return unitWeightQuantity;
    }

    public void setUnitWeightQuantity(String unitWeightQuantity) {
        this.unitWeightQuantity = unitWeightQuantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

  /*  public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }*/

    @Override
    public String toString() {
        return "ProductCreate{" +
                "productCategory='" + productCategory + '\'' +
                ", productType='" + productType + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", productSupplier='" + productSupplier + '\'' +
                ", productTile='" + productTile + '\'' +
                ", productSize='" + productSize + '\'' +
                ", unitType='" + unitType + '\'' +
                ", unitQuantity='" + unitQuantity + '\'' +
                ", unitWeightType='" + unitWeightType + '\'' +
                ", unitWeightQuantity='" + unitWeightQuantity + '\'' +
                ", unitPrice='" + unitPrice + '\'' +
                ", totalQuantity='" + totalQuantity + '\'' +
                ", totalPrice='" + totalPrice + '\'' +
                ", productDescription='" + productDescription + '\'' +
                '}';
    }
}
