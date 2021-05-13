package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class ProductOption extends BaseEntity {

	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private int price;
	@Column(nullable = false)
	private int quantity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	public void initProduct(Product product) {
		this.product = product;
	}
	
	public void minusQuantity(int count) {
		if (this.quantity - count < 0) {
			throw new IllegalArgumentException("재고가 부족합니다.");
		}
		this.quantity -= count;
	}
	public void addQuantity(int count) {
		this.quantity += count;
	}
}
