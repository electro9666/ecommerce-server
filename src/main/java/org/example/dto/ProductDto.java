package org.example.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.example.constant.ProductStatus;
import org.example.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
	private Long id;

	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	private String background;
	
	private int price;
	private ProductStatus status;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;

	private Long storeId;
	private String storeName;
	private Long categoryId;
	private List<ProductOptionDto> options;
	
	public ProductDto(Product product) {
		if (product == null) return;
		this.id = product.getId();
		this.name = product.getName();
		this.description = product.getDescription();
		this.status = product.getStatus();
		this.background = product.getBackground();
		this.price = product.getPrice();
	
		this.storeId = product.getStore().getId();
		this.storeName = product.getStore().getName();
		this.categoryId = product.getCategory().getId();
		this.options = product.getOptions().stream().map(t -> new ProductOptionDto(t)).collect(Collectors.toList());
		
//		TODO 에러 발생: java.lang.IllegalAccessException: Class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor can not access a member of class org.example.entity.BaseEntity with modifiers "public"
		// 해결: Cart에서 @EntityGraph를 이용해서 proxy가 아니라 실객체를 가져오도록
		this.regDate = product.getRegDate();
		this.updateDate = product.getUpdateDate();
	}
}
