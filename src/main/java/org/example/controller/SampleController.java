package org.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/sample")
@RestController
public class SampleController {

	@GetMapping(path = "/all")
	public String all() {
		return "all123";
	}
	@PostMapping(path = "/post/{id}")
	public Map<String, String> all2(@PathVariable("id") long id, @RequestBody Map<String, String> k) {
		System.out.println("id: " + id);
		System.out.println("body: " + k);
		Map<String, String> map = new HashMap<>();
		map.put("code", "hhh22");
		map.put("message", "12322");
		return map;
	}
}
