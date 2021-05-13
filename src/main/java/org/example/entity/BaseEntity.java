package org.example.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;


@Getter
@EntityListeners(value = { AuditingEntityListener.class })
@MappedSuperclass
public abstract class BaseEntity {

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime regDate;

	@LastModifiedDate
	private LocalDateTime updateDate;
}
