
package org.example.config;

import org.example.security.filter.ApiCheckFilter;
import org.example.security.filter.ApiLoginFilter;
import org.example.security.handler.ApiLoginFailHandler;
import org.example.security.service.LoginUserDetailsService;
import org.example.security.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.extern.log4j.Log4j2;

/**
 * 1~4: id/pw 로 로그인하기 5: oauth google
 */
@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${jwt.secret}")
    private String secretKey;
    
	// 1
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private LoginUserDetailsService loginUserDetailsService;

	// 11 - jwt
	@Bean
	public JWTUtil jwtUtil() {
		return new JWTUtil(secretKey);
	}

	// 11 - jwt
	@Bean
	public ApiCheckFilter apiCheckFilter() {
		return new ApiCheckFilter("/api/secure/**/*", jwtUtil());
	}

	// 11 - jwt
	@Bean
	public ApiLoginFilter apiLoginFilter() throws Exception {
		ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/common/login", jwtUtil());
		apiLoginFilter.setAuthenticationManager(authenticationManager());
		apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
		return apiLoginFilter;
	}

	// 3
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/sample/all/**")
			.permitAll()
			.antMatchers("/h2-console/*")
			.permitAll()
			.antMatchers("/api/secure/user/**")
			.hasRole("USER")
			.antMatchers("/api/secure/seller/**")
			.hasRole("SELLER")
			// TODO ADMIN
			;
		http.csrf().disable(); // 추가: csrf 토큰 기능 비활성화
		http.headers().frameOptions().disable(); // h2-console 때문에

		http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
		http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginUserDetailsService);
//		auth.eraseCredentials(false); // oauth에서 handler에서 password값을 가져오지 못하는 이유가 erase시키는 것 때문에. 기본이 true라서 false 시킴
	}

}
