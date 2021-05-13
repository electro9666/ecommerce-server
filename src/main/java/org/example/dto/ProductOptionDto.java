package org.example.dto;

import java.time.LocalDateTime;

import org.example.entity.ProductOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionDto {
	private Long id;
	private String name;
	private int price;
	private int quantity;

	public ProductOptionDto(ProductOption option) {
		this.id = option.getId();
		this.name = option.getName();
		this.price = option.getPrice();
		this.quantity = option.getQuantity();
	}
}
