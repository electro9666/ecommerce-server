package org.example.dto;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageResultDto<Entity, DTO> {

	// DTO리스트
	private List<DTO> result;

	// 총 페이지 번호
	private long total;

	public PageResultDto(Page<Entity> result, Function<Entity, DTO> fn) {
		this.result = result.stream().map(fn).collect(Collectors.toList());
		this.total = result.getTotalElements();
	}
}
