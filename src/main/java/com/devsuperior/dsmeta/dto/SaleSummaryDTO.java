package com.devsuperior.dsmeta.dto;

public class SaleSummaryDTO {

	private String sellerName;
	private Double sum;

	public SaleSummaryDTO(String sellerName, Double sum) {
		this.sellerName = sellerName;
		this.sum = sum;
	}

	public String getSellerName() {
		return sellerName;
	}

	public Double getSum() {
		return sum;
	}
}
