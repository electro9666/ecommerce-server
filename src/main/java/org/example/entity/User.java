package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.example.constant.UserGrade;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class User extends BaseEntity {

	@Id
	private Long id;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private int cash;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserGrade userGrade;

	@Embedded
	private Address address;

	public void minusCash(int price) {
		if (this.cash - price < 0) {
			throw new IllegalArgumentException("cash가 부족합니다.");
		}
		this.cash -= price;
	}

	public void addCash(int price) {
		this.cash += price;
	}
}
