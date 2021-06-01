package org.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.security.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.sentry.Sentry;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/sample")
@RestController
public class SampleController {

	@Autowired private JWTUtil jwtUtil;
	
	@GetMapping(path = "/all")
	public String all() {
		return "all123";
	}
	
	@GetMapping(path = "/token")
	public String token() {
		List<String> roles = new ArrayList<>(Arrays.asList("ROLE_USER"));

        String token = null;
        try {
            token = "Bearer " + jwtUtil.generateToken(1L, "1111", roles);
        } catch(Exception e) {
        	
        }
		return token;
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
	
	@GetMapping(path = "/get/{id}")
	public Map<String, Object> all3(@PathVariable("id") long id, @RequestParam(name = "name", required = false) String name, @RequestParam(name = "age", required = false) String age) {
		Map<String, Object> map = new HashMap<>();
		map.put("code", "hhh22");
		map.put("message", "12322");
		map.put("id", id);
		map.put("name", name);
		map.put("age", age);
		return map;
	}

	@GetMapping(path = "/sentry-test")
	public String sentryTest() {
		try {
			throw new Exception("This is a test.");
		} catch (Exception e) {
			Sentry.captureException(e);
		}
		return "sentry";
	}
}
