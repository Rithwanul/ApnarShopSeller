package com.bikroybaba.seller.model.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttributeProductEditResponse{

	@SerializedName("attributeName")
	@Expose
	private String attributeName;

	@SerializedName("attribute")
	@Expose
	private List<AttributeItem> attribute;

	public String getAttributeName(){
		return attributeName;
	}

	public void setAttributeName(String attributeName){
		this.attributeName = attributeName;
	}

	public List<AttributeItem> getAttribute(){
		return attribute;
	}

	public void setAttribute(List<AttributeItem> attribute){
		this.attribute = attribute;
	}
}