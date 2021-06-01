package org.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ApiModel(value = "카테고리", description = "카테고리.......")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Category extends BaseEntity {

	@ApiModelProperty(value = "아이디")
    @Id
    @GeneratedValue
    private Long id;
    
	@ApiModelProperty(value = "이름")
    @Column(nullable = false)
    private String name;
}
