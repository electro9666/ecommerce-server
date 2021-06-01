package org.example.controller;

import java.util.List;

import org.example.entity.Category;
import org.example.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;

@Api(tags = "Common")
@RequiredArgsConstructor
@RequestMapping("/api/secure/common")
@RestController
public class CommonController {
	
	private final CommonService commonService;
	
	@ApiOperation(value = "카테고리 목록", notes = "<big>카테고리 목록</big>을 반환한다.")
	@GetMapping("/category")
	public List<Category> category() {
		return commonService.category();
	}
}
