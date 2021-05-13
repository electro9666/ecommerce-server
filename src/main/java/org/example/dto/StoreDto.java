package org.example.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.example.constant.StoreStatus;
import org.example.entity.Store;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
	private Long id;
	
	@NotBlank // null, "", " " 허용X
	private String name;
	
	private StoreStatus status;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	
	public StoreDto(Store store) {
		if (store == null) {
			return;
		}
		this.id = store.getId();
		this.name = store.getName();
		this.status = store.getStatus();
		this.regDate = store.getRegDate();
		this.updateDate = store.getUpdateDate();
	}
}
