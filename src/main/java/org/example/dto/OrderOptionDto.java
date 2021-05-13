package org.example.dto;

import org.example.entity.OrderOption;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderOptionDto {
	private Long id;

	private CartDto cart;

	public OrderOptionDto(OrderOption orderCart) {
		if (orderCart == null) return;
		this.id = orderCart.getId();
		this.cart = new CartDto(orderCart.getCart());
	}
}
