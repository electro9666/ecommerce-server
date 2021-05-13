package org.example.controller;

import org.example.service.CommonService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/secure/common")
@RestController
public class CommonController {
	
	private final CommonService commonService;
	
	@GetMapping("/category")
	public Object category() {
		return commonService.category();
	}
}
