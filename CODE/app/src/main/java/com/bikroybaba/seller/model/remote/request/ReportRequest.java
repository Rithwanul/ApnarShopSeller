package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReportRequest {

    @SerializedName("categoryId")
    @Expose
    private final String categoryId;
    @SerializedName("typeId")
    @Expose
    private final String productTypeId;
    @SerializedName("productId")
    @Expose
    private final String productId;
    @SerializedName("timeline")
    @Expose
    private final String timeline;
    @SerializedName("startDate")
    @Expose
    private final String startDate;
    @SerializedName("endDate")
    @Expose
    private final String endDate;


    public ReportRequest(String categoryId, String productTypeId, String productId, String timeline, String startDate, String endDate) {
        this.categoryId = categoryId;
        this.productTypeId = productTypeId;
        this.productId = productId;
        this.timeline = timeline;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
