package org.example.dto;

import org.example.constant.ProductStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageRequestDto {
	private Integer page;
	private Integer take;
	
	// product
	private String productName;
	private ProductStatus productStatus;
	
	// sort
	private String sort;
	
	public Integer getPage() {
		if (this.page == null) {
			return 1;
		}
		return this.page;
	}
	public Integer getTake() {
		if (this.take == null) {
			return 10;
		}
		return this.take;
	}
}
