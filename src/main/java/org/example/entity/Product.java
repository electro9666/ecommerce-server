package org.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.example.constant.ProductStatus;
import org.hibernate.annotations.BatchSize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SequenceGenerator(name="SEQ_PRODUCT", sequenceName = "DB_SEQ_PRODUCT", initialValue = 1, allocationSize = 1)
@ToString(exclude = {"options"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Product extends BaseEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_PRODUCT")
	private Long id;
	@Column(nullable = false, unique = true)
	private String name;
	@Column(nullable = false)
	private String description; // TODO CLOB(textarea or html)
	private String background; // css style
	@Column(nullable = false)
	private int price; // 대표 가격(options의 첫번째) // 정렬할 때 사용함.
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CATEGORY_ID", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "STORE_ID", nullable = false)
	private Store store;
	
	// TODO orphanRemoval: initOptions로 전체를 갈아끼우니 detached entity passed to persist 에러 발생(cascade = CascadeType.ALL 추가함)
	@BatchSize(size = 100)
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductOption> options = new ArrayList<>();

	public void initOptions(List<ProductOption> options) {
		this.options = options;
	}
}
