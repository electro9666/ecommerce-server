package org.example.config;

import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.TestService;

@Configuration
public class SampleConfig {
	@Autowired UserService userService;
	
	@Bean
	public TestService myService() {
		TestService service = new TestService();
		service.setUserService(userService);
		return service;
	}
}
