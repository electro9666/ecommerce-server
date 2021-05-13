package org.example.dto;

import org.example.entity.CartOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartOptionDto {
	private Long id;
	private int price;
	private int count;
	
	private Long optionId;
	
	public CartOptionDto(CartOption cartOption) {
		this.id = cartOption.getId();
		this.price = cartOption.getPrice();
		this.count = cartOption.getCount();
		
		this.optionId = cartOption.getOption().getId();
	}
}
