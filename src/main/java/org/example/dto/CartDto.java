package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.entity.Cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
	private Long id;
	
	private Long productId;
	private ProductDto product;
	private List<CartOptionDto> cartOptions;
	
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	
	public CartDto(Cart cart) {
		if (cart == null) return;
		this.id = cart.getId();
		this.product = new ProductDto(cart.getProduct());
		this.cartOptions = cart.getCartOptions().stream().map(t -> new CartOptionDto(t)).collect(Collectors.toList());
		
		this.regDate = cart.getRegDate();
		this.updateDate = cart.getUpdateDate();
	}
}
