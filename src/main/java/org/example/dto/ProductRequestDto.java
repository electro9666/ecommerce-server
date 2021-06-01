package org.example.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.example.constant.ProductStatus;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ProductRequestDto {

	@ApiModel(value = "Product.Post")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {
		private Long storeId;
		private Long categoryId;
		
		@NotBlank
		private String name;
		
		@NotBlank
		private String description;
		
		private String background;
		
		private ProductStatus status;
		private List<ProductOptionDto> options;
	}
	
	@ApiModel(value = "Product.Put")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Put extends Post {
		private Long id;
	}
}
