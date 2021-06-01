package org.example.dto;

import lombok.Data;

@Data
public class PageSearchProductRequest extends PageBaseRequest {
	// product
	private String productName;
	// sort
	private String sort;
	
	public PageSearchProductRequest(Integer page, Integer take, String sort, String productName) {
		super(page, take);
		this.productName = productName;
		this.sort = sort;
	}
}
