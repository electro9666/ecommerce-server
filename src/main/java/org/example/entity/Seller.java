package org.example.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class Seller extends BaseEntity {

	@Id
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
//	@OneToMany(fetch = FetchType.LAZY, mappedBy = "seller")
//	private List<Store> stores = new ArrayList<>();
}
