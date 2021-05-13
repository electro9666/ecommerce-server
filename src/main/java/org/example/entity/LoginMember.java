package org.example.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import org.example.constant.LoginRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class LoginMember extends BaseEntity {

	@Id
	private Long id; // DB pk

	private String username; // 로그인 id
	private String password; // 로그인 pw

	private String name;

	private boolean fromSocial;

	@Builder.Default
	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "LOGIN_ROLE_SET", joinColumns = @JoinColumn(name = "ID")) // ROLE_SET 테이블의 EMAIL 컬럼
	@Column(name="ROLE_NAME", nullable = false, length = 10) // ROLE_SET 테이블의 ROLE_NAME 컬럼
	private Set<String> roleSet = new HashSet<String>();

	public void addMemberRole(LoginRole loginMemberRole) {
		roleSet.add(loginMemberRole.toString());
	}

}
