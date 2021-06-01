package org.example.dto;

import org.example.constant.StoreStatus;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class StoreRequestDto {
	
	@ApiModel(value = "Store.Post")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Post {
		private String name;
		private StoreStatus status;
	}
	
	@ApiModel(value = "Store.Put")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Put extends Post {
		private Long id;
	}
}
