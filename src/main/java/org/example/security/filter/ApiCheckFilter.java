package org.example.security.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.example.security.dto.LoginMemberDTO;
import org.example.security.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;


@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher;
    private String pattern;
    private JWTUtil jwtUtil;

    public ApiCheckFilter(String pattern, JWTUtil jwtUtil){
        this.antPathMatcher = new AntPathMatcher();
        this.pattern = pattern;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("REQUESTURI: " + request.getRequestURI());

        log.info(antPathMatcher.match(pattern, request.getRequestURI()));

        if(antPathMatcher.match(pattern, request.getRequestURI())) {

            log.info("ApiCheckFilter.................................................");
            log.info("ApiCheckFilter.................................................");
            log.info("ApiCheckFilter.................................................");

            Authentication authentication = checkAuthHeader(request);
            System.out.println("authentication?" + authentication);

            if(authentication != null){
                SecurityContext context = SecurityContextHolder.getContext();
                context.setAuthentication(authentication);
                
                filterChain.doFilter(request, response);
                return;
            }else {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                // json 리턴
                response.setContentType("application/json;charset=utf-8"); // 한글깨짐 방지
                JSONObject json = new JSONObject();
                String message = "FAIL CHECK API TOKEN";
                json.put("code", "403");
                json.put("message", message);

                PrintWriter out = response.getWriter();
                out.print(json);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private Authentication checkAuthHeader(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");
        System.out.println("authHeader:" + authHeader);

//        if(StringUtils.hasText(authHeader)) {
//        	if (authHeader.equals("12345678")) {
//        		checkResult = true;
//        	}
//        }
        		
        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            log.info("Authorization exist: " + authHeader);

            try {
//            	String id = jwtUtil.validateAndExtract(authHeader.substring(7));
                Claims claims = jwtUtil.getClaims(authHeader.substring(7));
                Integer id = (Integer) claims.get("id");
                String username = (String) claims.get("username");
                
                Set<GrantedAuthority> roles = new HashSet<>();
                List<String> list = (List<String>) claims.get("roles");
                for (String role : list) {
                	roles.add(new SimpleGrantedAuthority(role));
				}
                log.info("validate result: " + username);
                if (username.length() > 0) {
                	return new UsernamePasswordAuthenticationToken(new LoginMemberDTO(id.longValue(), username, "[TEMPORARY]", null, false, roles), null, roles); // 중요한건 마지막 roles이다.
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
