package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Supplier {
    @SerializedName("supplierId")
    @Expose
    private String supplierId;
    @SerializedName("supplierTitle")
    @Expose
    private String supplierTitle;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierTitle() {
        return supplierTitle;
    }

    public void setSupplierTitle(String supplierTitle) {
        this.supplierTitle = supplierTitle;
    }
}
