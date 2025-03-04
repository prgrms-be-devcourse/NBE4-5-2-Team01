package com.team01.project.domain.user.entity;

import java.util.UUID;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "USER")
public class User {
	@Id
	@Column(name = "USER_UUID")
	@Builder.Default
	private final String id = "user-" + UUID.randomUUID();

	@Email
	@Column(name="USER_EMAIL")
	private String email;

	@Column(name="NAME")
	private String name;

	@Column(name="Nickname")
	private String nickName;

	@Column(name="birthday")
	private String birthDay;

	@CreatedDate
	@Column(name ="CREATED_AT")
	private String createdDate;

	@Column(name = "Field")
	private String field;

	@Column(name = "follow_id")
	private long followId;

}
