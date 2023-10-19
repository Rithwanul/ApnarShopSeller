package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeItem{

	@SerializedName("attributeDataId")
	@Expose
	private int attributeDataId;

	@SerializedName("attributeData")
	@Expose
	private String attributeData;

	public int getAttributeDataId(){
		return attributeDataId;
	}

	public void setAttributeDataId(int attributeDataId){
		this.attributeDataId = attributeDataId;
	}

	public String getAttributeData(){
		return attributeData;
	}

	public void setAttributeData(String attributeData){
		this.attributeData = attributeData;
	}
}