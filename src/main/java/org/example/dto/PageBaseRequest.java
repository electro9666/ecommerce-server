package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageBaseRequest {
	private Integer page;
	private Integer take;
	
	public Integer getPage() {
		if (this.page == null) {
			return 1;
		}
		return this.page;
	}
	public Integer getTake() {
		if (this.take == null) {
			return 10;
		}
		return this.take;
	}
}
