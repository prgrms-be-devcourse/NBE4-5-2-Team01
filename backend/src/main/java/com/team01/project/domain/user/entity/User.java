package com.team01.project.domain.user.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "user_tbl")
public class User {
	@Id
	@Column(name = "user_id")
	private String id;

	@Email
	private String email;

	private String name;

	private String nickName;

	@Column(name = "birthday")
	private LocalDate birthDay;

	@CreatedDate
	@Column(name = "create_at")
	private LocalDateTime createdDate;

	private String field;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RefreshToken> refreshTokens;
}
