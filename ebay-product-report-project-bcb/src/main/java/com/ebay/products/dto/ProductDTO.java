package com.ebay.products.dto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.annotations.SerializedName;

public class ProductDTO {

	@SerializedName("fullName")
	private String fullName;
	@SerializedName("itemDetails")
	private String itemDetails;
	@SerializedName("decision")
	private String decision;
	@SerializedName("productLink")
	private String productLink;

	public ProductDTO(String fullName, String itemDetails, String decision, String productLink) {
		this.fullName = fullName;
		this.itemDetails = itemDetails;
		this.decision = decision;
		this.productLink = productLink;
	}

	public ProductDTO() {
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getItemDetails() {
		return itemDetails;
	}

	public void setItemDetails(String itemDetails) {
		this.itemDetails = itemDetails;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getProductLink() {
		return productLink;
	}

	public void setProductLink(String productLink) {
		this.productLink = productLink;
	}

	public static List<Map<String, String>> convertToMap(List<ProductDTO> productDTOs) {
		return productDTOs.stream().map(product -> {
			Map<String, String> productMap = new HashMap<>();
			productMap.put("fullName", product.getFullName());
			productMap.put("productLink", product.getProductLink());
			productMap.put("decision", product.getDecision());
			productMap.put("itemDetails", product.getItemDetails());

			return productMap;
		}).collect(Collectors.toList());
	}
}
