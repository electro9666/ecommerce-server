package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.example.constant.OrderStatus;
import org.example.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private Long id;
	
	private Long userId;
//	private Seller seller;
	private OrderStatus status;
	private int totalPrice;
	private List<OrderOptionDto> orderCart;

	private LocalDateTime orderDate;
	
	public OrderDto(Order order) {
		this.id = order.getId();
		this.userId = order.getUser().getId();
//		this.seller = order.getSeller();
		this.status = order.getOrderStatus();
		this.totalPrice = order.getTotalPrice();
		this.orderCart = order.getOrderCarts().stream().map(t -> new OrderOptionDto(t)).collect(Collectors.toList());
		this.orderDate = order.getOrderDate();
	}
}
