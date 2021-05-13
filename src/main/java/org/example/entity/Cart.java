package org.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.example.constant.CartStatus;
import org.hibernate.annotations.BatchSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"cartOptions"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Cart extends BaseEntity {

	@Id @GeneratedValue
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	private Product product;
	
	@BatchSize(size = 100)
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<CartOption> cartOptions = new ArrayList<>();
	
	@Enumerated(EnumType.STRING)	
	private CartStatus status;
	
	public void initCartOptions(List<CartOption> cartOptions) {
		this.cartOptions = cartOptions;
	}
	public void changeStatus(CartStatus status) {
		this.status = status;
	}
}
