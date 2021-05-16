package org.example.security.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.security.dto.LoginMemberDTO;
import org.example.security.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.common.contenttype.ContentType;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final String LoginMemberDTO = null;
	private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {

        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("-----------------ApiLoginFilter---------------------");
        log.info("attemptAuthentication");

        // 모든 method가 다 들어올수 있다. GET,POST,PUT,DELETE,PATCH,,,
        if (request.getHeader("Content-Type") == null) {
        	// params가 없으면 아예 content-type자체가 없다.
        	throw new AuthenticationServiceException("Content-Type is null");
        }
        // application/json으로 전달하는 경우
        HashMap<String, String> jsonRequest = null;
        if (request.getHeader("Content-Type").contains(ContentType.APPLICATION_JSON.getType())) {
        	ObjectMapper mapper = new ObjectMapper();
        	try {
        		jsonRequest = (HashMap<String, String>) mapper.readValue(request.getReader().lines().collect(Collectors.joining()), new TypeReference<Map<String, String>>() {});
        	} catch (IOException e) {
        		e.printStackTrace();
        		throw new AuthenticationServiceException("application/json parsing error");
        	}
        }
        if (jsonRequest == null) {
        	throw new AuthenticationServiceException("jsonRequest is null");
        }
        String username = jsonRequest.get("username");
        String password = jsonRequest.get("password");
        
//        String id = request.getParameter("id");
//        String pw = request.getParameter("pw");

        System.out.println("username?" + username);
        System.out.println("password?" + password);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return getAuthenticationManager().authenticate(authToken);
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        log.info("-----------------ApiLoginFilter---------------------");
        log.info("successfulAuthentication: " + authentication);

        log.info(authentication.getPrincipal());

        LoginMemberDTO loginMemberDTO = (LoginMemberDTO) authentication.getPrincipal();
        Long id = loginMemberDTO.getId();
        String username = loginMemberDTO.getUsername();
        Collection<GrantedAuthority> authorities = loginMemberDTO.getAuthorities();
        List<String> roles = authorities.stream().map(t -> t.toString()).collect(Collectors.toList());

        String token = null;
        try {
            token = jwtUtil.generateToken(id, username, roles);

            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info(token);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}









