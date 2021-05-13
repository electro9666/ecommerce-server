package org.example.security.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Getter
@Setter
@ToString
public class LoginMemberDTO extends User /* implements Serializable */ {

	private Long id;
	
	private String username; // 로그인 id

	private String name;

	private boolean fromSocial;

	// id/pw 로 로그인
	public LoginMemberDTO(Long id, String username, String password, String name, boolean fromSocial,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = id;
		this.username = username;
		this.name = name;
		this.fromSocial = fromSocial;
	}

}
