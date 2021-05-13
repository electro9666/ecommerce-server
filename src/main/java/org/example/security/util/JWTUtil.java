package org.example.security.util;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class JWTUtil {

//	private Key key; //io.jsonwebtoken.security.Keys;
	private byte[] key;
	
	// 1month
	private long expire = 60 * 24 * 30;

	public JWTUtil(String secretKey) {
//		this.key = Keys.hmacShaKeyFor(secret.getBytes());
		try {
			this.key = secretKey.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public String generateToken(Long id, String username, List<String> roles) throws Exception {

		return Jwts.builder()
			.setIssuedAt(new Date())
			.setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
			// .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(1).toInstant()))
			.claim("id", id)
			.claim("username", username)
			.claim("roles", roles)
			.signWith(SignatureAlgorithm.HS256, this.key)
			.compact();
	}

//	public String validateAndExtract(String tokenStr) throws Exception {
//
//		String contentValue = null;
//
//		try {
//			DefaultJws defaultJws = (DefaultJws) Jwts.parser()
//				.setSigningKey(this.key)
//				.parseClaimsJws(tokenStr);
//
//			log.info(defaultJws);
//
//			log.info(defaultJws.getBody().getClass());
//
//			DefaultClaims claims = (DefaultClaims) defaultJws.getBody();
//
//			log.info("------------------------");
//
//			contentValue = claims.getSubject();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.error(e.getMessage());
//			contentValue = null;
//		}
//		return contentValue;
//	}

	public Claims getClaims(String token) {
	    return Jwts.parser()
		    .setSigningKey(this.key)
		    .parseClaimsJws(token)
		    .getBody();
		
	}

}
