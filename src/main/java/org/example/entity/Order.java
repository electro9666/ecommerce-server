package org.example.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.example.constant.OrderStatus;
import org.hibernate.annotations.BatchSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name="ORDERS")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SELLER_ID", nullable = false)
	private Seller seller;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;
	
	@Column(nullable = false)
	private int totalPrice;
	
	@BatchSize(size = 100)
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderOption> orderCarts = new ArrayList<>();

	@Column(nullable = false)
	private LocalDateTime orderDate;

	public void initOrderCarts(List<OrderOption> orderCarts) {
		this.orderCarts = orderCarts;
	}
	public void changeStatus(OrderStatus status) {
		this.orderStatus = status;
	}
}
