package com.bikroybaba.seller.http;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("data")
    @Expose
    private JsonElement data;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "code=" + code +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}