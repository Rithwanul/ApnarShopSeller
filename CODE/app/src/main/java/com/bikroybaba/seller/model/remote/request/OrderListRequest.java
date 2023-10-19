package com.bikroybaba.seller.model.remote.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OrderListRequest {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("keyword")
    @Expose
    private String keyword;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("searchKey")
    @Expose
    private String searchKey;

    public OrderListRequest() {
    }

    public OrderListRequest(String type, String keyword, String startDate, String endDate,String searchKey) {
        this.type = type;
        this.keyword = keyword;
        this.startDate = startDate;
        this.endDate = endDate;
        this.searchKey = searchKey;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getType() {
        return type;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
